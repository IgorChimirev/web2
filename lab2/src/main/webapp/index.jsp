<%@page contentType="text/html; charset=UTF-8" %>
<%-- <%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %> --%>
<html>
<head>
    <title>lab2</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        <%@include file="style.css" %>
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Чимирев Игорь Олегович</h2>
        <h3>Группа: P3222 | Номер варианта: 115341</h3>
    </div>

    <div class="main-content">
        <div class="input-side">
            <div class="form-block">
                <form id="valForm">
                    <div class="input-group">
                        <p>Координата X:</p>
                        <div class="checkbox-group">
                            <label><input type="checkbox" name="x" value="-2"><span>-2</span></label>
                            <label><input type="checkbox" name="x" value="-1.5"><span>-1.5</span></label>
                            <label><input type="checkbox" name="x" value="-1"><span>-1</span></label>
                            <label><input type="checkbox" name="x" value="-0.5"><span>-0.5</span></label>
                            <label><input type="checkbox" name="x" value="0"><span>0</span></label>
                            <label><input type="checkbox" name="x" value="0.5"><span>0.5</span></label>
                            <label><input type="checkbox" name="x" value="1"><span>1</span></label>
                            <label><input type="checkbox" name="x" value="1.5"><span>1.5</span></label>
                            <label><input type="checkbox" name="x" value="2"><span>2</span></label>
                        </div>
                    </div>

                    <div class="input-group">
                        <p>Координата Y:</p>
                        <input type="text" id="y" placeholder="(-3...3)" maxlength="8">
                    </div>

                    <div class="input-group">
                        <p>Радиус R:</p>
                        <div class="radio-group">
                            <label><input type="radio" name="r" value="1"><span>1</span></label>
                            <label><input type="radio" name="r" value="1.5"><span>1.5</span></label>
                            <label><input type="radio" name="r" value="2"><span>2</span></label>
                            <label><input type="radio" name="r" value="2.5"><span>2.5</span></label>
                            <label><input type="radio" name="r" value="3"><span>3</span></label>
                        </div>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="data-button">Отправить данные</button>
                        <button type="reset" class="data-button" onclick="resetForm()">Сбросить данные</button>
                        <button type="button" class="data-button" id="clear_table">Сбросить результаты</button>
                    </div>

                    <div id="message" class="input-log"></div>
                </form>
            </div>
        </div>

        <div class="graph-results-container">
            <div class="graph-side">
                <div class="graph" id="area">
                    <svg height="100%" width="100%" viewBox="0 0 300 300" preserveAspectRatio="xMidYMid meet" id="graph" xmlns="http://www.w3.org/2000/svg">
                        <!-- Оси со стрелками -->
                        <line x1="0" x2="300" y1="150" y2="150" stroke="gray"></line>
                        <line x1="150" x2="150" y1="0" y2="300" stroke="gray"></line>
                        <polygon class="arrow" points="150,0 144,15 156,15" fill="gray"></polygon>
                        <polygon class="arrow" points="300,150 285,156 285,144" fill="gray"></polygon>

                        <!-- Засечки -->
                        <line x1="200" x2="200" y1="155" y2="145" stroke="gray"></line>
                        <line x1="250" x2="250" y1="155" y2="145" stroke="gray"></line>
                        <line x1="50" x2="50" y1="155" y2="145" stroke="gray"></line>
                        <line x1="100" x2="100" y1="155" y2="145" stroke="gray"></line>
                        <line x1="145" x2="155" y1="100" y2="100" stroke="gray"></line>
                        <line x1="145" x2="155" y1="50" y2="50" stroke="gray"></line>
                        <line x1="145" x2="155" y1="200" y2="200" stroke="gray"></line>
                        <line x1="145" x2="155" y1="250" y2="250" stroke="gray"></line>

                        <!-- Подписи к засечкам -->
                        <text x="195" y="140" fill="gray">R/2</text>
                        <text x="248" y="140" fill="gray">R</text>
                        <text x="40" y="140" fill="gray">-R</text>
                        <text x="90" y="140" fill="gray">-R/2</text>
                        <text x="160" y="105" fill="gray">R/2</text>
                        <text x="160" y="55" fill="gray">R</text>
                        <text x="160" y="205" fill="gray">-R/2</text>
                        <text x="160" y="255" fill="gray">-R</text>
                        <text x="160" y="15" fill="gray">Y</text>
                        <text x="285" y="140" fill="gray">X</text>

                        <!-- Области -->
                        <rect class="area" x="50" y="150" width="100" height="100" fill="#0a0eff" fill-opacity="0.1" stroke="#0c0fff"></rect>
                        <polygon class="area" points="50,150 150,150 150,50" fill="#0a0eff" fill-opacity="0.1" stroke="#0c0fff"></polygon>
                        <path class="area" d="M 250 150 A 100, 100 0, 0,1, 150, 250 L 150 150 Z" fill="#0a0eff" fill-opacity="0.1" stroke="#0c0fff"></path>
                    </svg>
                </div>
            </div>

            <div class="results-side">
                <h3>Результаты проверки</h3>
                <div class="table-container">
                    <table id="result_table">
                        <thead>
                        <tr>
                            <th>Координата X</th>
                            <th>Координата Y</th>
                            <th>Радиус R</th>
                            <th>Попал/не попал</th>
                            <th>Текущее время</th>
                            <th>Время выполнения (ms)</th>
                        </tr>
                        </thead>
                        <tbody id="resultBody">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="script.js"></script>
</body>
</html>