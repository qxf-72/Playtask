import json
import sqlite3
from datetime import datetime
from pathlib import Path

source = Path("old-playtask.db")
target = Path("旧版数据迁移.json")
db = sqlite3.connect(source)
db.row_factory = sqlite3.Row


def stamp(milliseconds):
    return datetime.fromtimestamp(milliseconds / 1000).strftime("%Y-%m-%d %H:%M:%S")


tasks = []
for row in db.execute("SELECT * FROM TASK WHERE DELETED=0 ORDER BY ID"):
    tasks.append({
        "id": row["ID"], "time": stamp(row["CREATED_TIME"]),
        "name": row["TITLE"], "score": row["SCORE"],
        "type": row["TYPE"] if row["TYPE"] in (0, 1, 2) else 2,
        "total_amount": max(1, row["LOOP"]), "finished_amount": 0,
    })

rewards = []
reward_counts = dict(db.execute(
    "SELECT WISH,COUNT(*) FROM WISH_HISTORY WHERE DELETED=0 AND CANCELED=0 GROUP BY WISH"
))
for row in db.execute("SELECT * FROM WISH WHERE DELETED=0 ORDER BY ID"):
    rewards.append({
        "id": row["ID"], "time": stamp(row["CREATED_TIME"]),
        "name": row["TITLE"], "score": abs(row["SCORE"]),
        "type": 2 if row["LOOP"] == 0 else 1,
        "finished_amount": reward_counts.get(row["ID"], 0),
    })

task_by_id = {row["ID"]: row for row in db.execute("SELECT * FROM TASK")}
wish_by_id = {row["ID"]: row for row in db.execute("SELECT * FROM WISH")}
ledger = []
for row in db.execute("SELECT * FROM TASK_HISTORY WHERE DELETED=0 ORDER BY COMPLETION_TIME,ID"):
    if row["CANCELED"]:
        continue
    task = task_by_id.get(row["TASK"])
    if task is None:
        continue
    points = task["SCORE"]
    title = "完成任务：" + task["TITLE"]
    ledger.append((row["COMPLETION_TIME"], title, points))
for row in db.execute("SELECT * FROM WISH_HISTORY WHERE DELETED=0 ORDER BY SATISFIED_TIME,ID"):
    wish = wish_by_id.get(row["WISH"])
    if wish is None:
        continue
    points = -abs(wish["SCORE"]) * (-1 if row["CANCELED"] else 1)
    title = ("撤销奖励：" if row["CANCELED"] else "兑换奖励：") + wish["TITLE"]
    ledger.append((row["SATISFIED_TIME"], title, points))
ledger.sort(key=lambda event: event[0])
count_info = [
    {"id": index, "time": stamp(when), "name": name, "score": points}
    for index, (when, name, points) in enumerate(ledger, 1)
]

root = {
    "format": "PlayTaskBackup", "version": 1,
    "data": {"task_info": tasks, "reward_info": rewards, "count_info": count_info},
}
target.write_text(json.dumps(root, ensure_ascii=False, indent=2), encoding="utf-8")
actual = sum(item["score"] for item in count_info)
expected = db.execute("SELECT SCORE FROM USER WHERE DELETED=0 ORDER BY ID LIMIT 1").fetchone()[0]
print(json.dumps({"file": str(target.resolve()), "tasks": len(tasks), "rewards": len(rewards),
                  "task_history": sum(1 for row in db.execute("SELECT ID FROM TASK_HISTORY WHERE DELETED=0")),
                  "reward_history": sum(1 for row in db.execute("SELECT ID FROM WISH_HISTORY WHERE DELETED=0")),
                  "ledger_entries": len(count_info), "ledger_total": actual, "old_balance": expected},
                 ensure_ascii=False, indent=2))
if actual != expected:
    raise SystemExit("Converted ledger does not match old-app balance")
