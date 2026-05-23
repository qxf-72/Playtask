# 简介


**技术栈： Java、Android、SQLite、RecyclerView、ViewModel、MPAndroidChart**

基于 Java 原生 Android 开发的任务管理应用，支持任务创建、任务完成、积分奖励、奖励兑换和积分统计等功能。项目通过 SQLite 存储任务、奖励和积分流水数据，并使用图表展示用户积分收支情况。


---

<br/>


<br/>



# 使用

仓库存储 app 的 Android Studio工程文件夹，可clone到本地后使用 Android Studio 打开。

导出的 apk 文件已发布到 release ，可在 release 下载到安卓设备上安装使用。

---


<br/>


<br/>


# 演示

## 任务界面

**新建任务**

点击右上角"+"号按钮，选择"新建任务"，输入任务详细信息进行新建。

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214332.png" width = 600 /> </div>


**任务的完成**

点击 checkbox 以表示完成任务，任务完成之后，任务完成次数+1（如果达到设定的最多完成次数，任务在界面上消失），积分会产生响应变化。

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214515.png" width = 600 /> </div>


**任务的排序**

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_214834.png" width = 600 /> </div>


**任务的编辑**

单击任务，跳转到编辑页面。

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215014.png" width = 600 /> </div>


**任务的删除**

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215106.png" width = 600 /> </div>


## 奖励界面

**奖励的消耗**

单击奖励项，确认之后，消耗积分，如果是单次奖励，奖励项会消失，如果是多次奖励，使用次数加一。

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215310.png" width = 600 /> </div>

“奖励”页面其余功能和“任务”界面类似，不再一一演示。


## 统计界面

统计页面下属有四个子页面，分别是对于 当日、当周、当月、当年的数据，进行积分收入、支出、结余 的统计，并绘制折线图。

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215551.png" width = 600 /> </div>

<div align="center"> <img src="https://picture-in-md.oss-cn-guangzhou.aliyuncs.com/2024-01-09_215631.png" width = 600 /> </div>

---
