<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath %>">
    
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Nbx4Q9PtGhrRgkGdlvbgLaZm"></script>
    <title>生活小助手-步行导航检索</title>
</head>
<%
    String p1 = request.getParameter("p1");
    String p2 = request.getParameter("p2");
%>
<body>
    <div id="allmap"></div>
</body>
<script type="text/javascript">
    var p1 = new BMap.Point(<%=p1%>);
    var p2 = new BMap.Point(<%=p2%>);
    // 百度地图API功能
    var map = new BMap.Map("allmap");
    map.centerAndZoom(new BMap.Point((p1.lng+p2.lng)/2, (p2.lat+p1.lat)), 11);
    var walking = new BMap.WalkingRoute(map, {renderOptions:{map: map, autoViewport: true}});
    walking.search(p1, p2);
</script>
</html>