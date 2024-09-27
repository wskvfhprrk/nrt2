# 牛肉汤自动售卖机程序

牛肉汤自动售卖机程序是一个集成了前后端的自动售卖系统，旨在提供用户友好的售卖体验。

## 项目结构

本项目的目录结构如下：


- **src**: 包含了项目的后台源码，主要负责处理业务逻辑和数据交互。
- **target**: 存放项目测试打包文件，该目录中的文件不会被提交到代码库中。
- **web**: 包含了项目前端的源码，主要负责用户界面和交互。
- **dist**: 前端的打包产物目录，该目录中的文件不会被提交到代码库中。

## 使用说明

1. 克隆仓库到本地：
    ```bash
    git clone <repository-url>
    ```
2. 进入项目总目录：
    ```bash
    cd nrt2
    ```
3. 安装依赖并运行项目：
    - 后台：
        ```bash
        cd src
        # 安装依赖
        # 启动后台服务
        ```
    - 前端：
        ```bash
        cd web
        # 安装依赖
        npm install 
        # 启动前端服务
        npm run serve
        # 打包程序
        npm run build
        ```



## windows中使用谷歌浏览器全屏打开首页

1.安装谷歌浏览器`chome`;
2.在c盘根目录配置目录文件：`c:\scripts\open_browser.ps1`，内容：
```bash
Start-Process "C:\Program Files\Google\Chrome\Application\chrome.exe" "--start-fullscreen http://localhost:8080"
```
前面是谷歌浏览器的路径——根据电脑安装目前进行配置，后面是打开乎页面的访问路径，所配置的本地路径`http://localhost:8080`访问本地服务器8080端口即为首页订单页面。
3.配置启动批处理文件`start_my_java_service.bat`，路径与项目方打包文件放在一起。内容：
```bash
    @echo off
    # 使用utf-8编码cmd启动服务，防止弹出窗口乱码
    chcp 65001
    echo 正在启动服务...
    # 使用utf-8启动服务，-jar 后面的xx.jar项目启动文件，与打包项目名一致。
    java -Dfile.encoding=UTF-8 -jar junchuang-0.0.1-SNAPSHOT.jar
```
4.桌面上建立一个快捷方式指向`start_my_java_service.bat`，并改名为启动项目。


## 贡献指南

欢迎任何形式的贡献！请遵循以下步骤参与项目：

1. Fork 这个仓库
2. 创建您的功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 架构

### mqtt心跳机制

