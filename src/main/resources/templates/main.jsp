<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<!--
Editorial by HTML5 UP
html5up.net | @ajlkn
Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>"${path}"</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" href="${path}/assets/css/main.css" />
    <style>
        .filebox input[type="file"] {
            position: absolute;
            width: 0;
            height: 0;
            padding: 0;
            overflow: hidden;
            border: 0;
        }

        .filebox label {
            display: inline-block;
            padding: 5px 10px;
            color: #fff;
            vertical-align: middle;
            background-color: #f56a6a;
            cursor: pointer;
            height: 30px;
            margin-left: 10px;
            margin-top: 10px;
        }
        .submitbutton {
            display: inline-block;
            color: #fff;
            vertical-align: middle;
            background-color: #000000;
            cursor: pointer;
            height: 30px;
            margin-left: 10px;
            margin-top: 10px;
        }
        .filebox .upload-name {
            display: inline-block;
            height: 40px;
            padding: 0 5px;
            vertical-align: middle;
            border: 1px solid #dddddd;
            width: 58%;
            color: #999999;
        }
        button{
            background:#1AAB8A;
            color:#fff;
            border:none;
            position:relative;
            height:60px;
            font-size:1.6em;
            padding:0 2em;
            cursor:pointer;
            transition:800ms ease all;
            outline:none;
        }
        button:hover{
            background:#fff;
            color:#FFEBEB;
        }
        button:before,button:after{
            content:'';
            position:absolute;
            top:0;
            right:0;
            height:2px;
            width:0;
            background: #FFEBEB;
            transition:400ms ease all;
        }
        button:after{
            right:inherit;
            top:inherit;
            left:0;
            bottom:0;
        }
        button:hover:before,button:hover:after{
            width:100%;
            transition:800ms ease all;
        }
    </style>
</head>
<body class="is-preload">
<div>"${path}"</div>
<!-- Wrapper -->
<div id="wrapper">

    <!-- Main -->
    <div id="main">
        <div class="inner">

            <!-- Header -->
            <header id="header">
                <a class="logo"><strong>Plagiarism check</strong>  result</a>
                <ul class="icons">
                    <li><a href="#" class="icon brands fa-twitter"><span class="label">Twitter</span></a></li>
                    <li><a href="#" class="icon brands fa-facebook-f"><span class="label">Facebook</span></a></li>
                    <li><a href="#" class="icon brands fa-snapchat-ghost"><span class="label">Snapchat</span></a></li>
                    <li><a href="#" class="icon brands fa-instagram"><span class="label">Instagram</span></a></li>
                    <li><a href="#" class="icon brands fa-medium-m"><span class="label">Medium</span></a></li>
                </ul>
            </header>

            <!-- Banner -->
            <section id="banner">
                <div class="content">
                    <header>
                        <h1>JPlag<br />
                            Detecting Software Plagiarism</h1>
                    </header>
                </div>
                <span class="image object">
										<img src="/static/images/logo.png" alt="" />
									</span>
            </section>


        </div>
    </div>

    <!-- Sidebar -->
    <div id="sidebar">
        <div class="inner">
            <div>
                <form class="filebox" method="post" enctype="multipart/form-data" action="/upload">

                    <input class="upload-name" value="????????????" placeholder="????????????">
                    <label for="uploadfile">????????????</label>
                    <input type="file" id="uploadfile" name="uploadfile" accept="*/*">
                    <button class="button" type = "submit">??????</button>
                </form>
            </div>


            <!-- Menu -->
            <nav id="menu">

                <header class="major">
                    <h2>Subject</h2>
                </header>
                <ul th:each="subject : ${subjects}">


                    <li>
											<span class="opener">
											<form>
												<input type="checkbox" class="chk" name="cchk" th:value="${subject.getId()}" onclick="" ><label>[[${subject.subjectName}]]</label>
												 <input type='submit'>
											</form>
												</span>
                        <ul>

                            <li th:each="assignment : ${assignments}" th:if="${subject.getId() == assignment.getSubjectDto().getId()}">
                                <a th:href="@{/result(assignmentName=${assignment.getAssignmentName()}, subjectName=${assignment.getSubjectDto().getSubjectName()})}">[[${assignment.assignmentName}]]</a>

                            </li>
                        </ul>
                    </li>


                </ul>
            </nav>


        </div>
    </div>

</div>

<!-- Scripts -->
<script src="/static/assets/js/jquery.min.js"></script>
<script src="/static/assets/js/browser.min.js"></script>
<script src="/static/assets/js/breakpoints.min.js"></script>
<script src="/static/assets/js/util.js"></script>
<script src="/static/assets/js/main.js"></script>
<script src="/static/assets/js/user.js"></script>

</body>
</html>