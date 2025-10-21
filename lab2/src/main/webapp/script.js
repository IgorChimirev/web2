
let responses = [];

document.getElementById('valForm').addEventListener('submit', function (e) {
    e.preventDefault();
    let xCheckBoxes = document.querySelectorAll('input[name="x"]:checked');
    let y = document.getElementById('y');
    let rRadios = document.querySelectorAll('input[name="r"]:checked');

    if (validate(xCheckBoxes, y, rRadios)) {
        let xElements = [];
        for (let index = 0; index < xCheckBoxes.length; index++) {
            if (xCheckBoxes[index].checked) {
                xElements.push(xCheckBoxes[index].value);
            }
        }
        send(xElements[0], y.value, rRadios[0].value, "form");
    }
});

document.querySelectorAll("input[name='r']").forEach(radio => {
    radio.addEventListener('change', function () {
        document.querySelectorAll("circle").forEach(point => point.remove());
        drawPoints(responses);
    });
});

document.getElementById('clear_table').addEventListener('click', function (e) {
    fetch("clear", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({})
    })
        .then(response => response.json())
        .then(data => {
            responses = data;
            showAll(data);
            document.querySelectorAll("circle").forEach(point => point.remove());
            drawPoints(data);
        }).catch(error => console.error('Error:', error));
    console.log('очищено')
});

document.getElementById('area').addEventListener('click', function (e) {
    const point = document.getElementById('graph').createSVGPoint();
    point.x = e.clientX;
    point.y = e.clientY;

    const svgPoint = point.matrixTransform(document.getElementById('graph').getScreenCTM().inverse());
    let rRadios = document.querySelectorAll('input[name="r"]:checked');

    if (rRadios.length > 0) {
        let rValue = rRadios[0].value;
        let userPointX = ((svgPoint.x - 150) / 100 * rValue).toFixed(2);
        let userPointY = ((150 - svgPoint.y) / 100 * rValue).toFixed(2);

        // Преобразуем в числа для проверки
        let xNum = parseFloat(userPointX);
        let yNum = parseFloat(userPointY);

        // Проверяем допустимость координат
        if (xNum < -2 || xNum > 2) {
            showError(document.getElementById('graph'), "Координата X должна быть в диапазоне от -2 до 2");
            return;
        }
        if (yNum < -3 || yNum > 3) {
            showError(document.getElementById('graph'), "Координата Y должна быть в диапазоне от -3 до 3");
            return;
        }

        console.log(`Координаты на плоскости: x=${userPointX}, y=${userPointY}, Координаты в svg: (${svgPoint.x.toFixed(2)}, ${svgPoint.y.toFixed(2)})`);
        send(userPointX, userPointY, rValue, "click");
    } else {
        showError(document.getElementById('graph'), "Необходимо выбрать значение радиуса");
    }
})

// Оставляем только один выбранный X (как было для Y)
document.querySelectorAll("input[name='x']").forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        document.querySelectorAll("input[name='x']").forEach(cb => {
            if (cb !== this) cb.checked = false;
        });
    });
});

function send(x, y, r, flag) {
    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const data = JSON.stringify({x: x, y: y, r: r, flag: flag, timezone: timeZone});
    console.log(data);
    fetch("pointCheck", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: data
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Network response was not ok');
            }
        })
        .then(data => {
            responses = data;
            showAll(data);
            document.querySelectorAll("circle").forEach(point => point.remove());
            drawPoints(data);
        }).catch(error => console.error('Error:', error));
}

function showAll(data) {
    const resultBody = document.getElementById('resultBody');
    resultBody.innerHTML = '';
    for (let i = 0; i < data.length; i++) {
        let newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${data[i].x}</td>
            <td>${data[i].y}</td>
            <td>${data[i].r}</td>
            <td>${data[i].value === 'true' ? 'точно в цель' : 'мимо'}</td>
            <td>${data[i].time}</td>
            <td>${data[i].execTime} ms</td>
        `;
        resultBody.appendChild(newRow);
    }
}

function showError(element, message) {
    const errorElement = document.createElement('div');
    errorElement.classList.add('error-message');
    errorElement.textContent = message;
    errorElement.style.color = 'red';
    errorElement.style.fontSize = '13px';
    errorElement.style.textAlign = 'center';
    element.parentNode.insertBefore(errorElement, element.nextSibling);
    setTimeout(function () {
        errorElement.remove();
    }, 3000);
}

function validate(xCheckBoxes, y, rRadios) {
    let xElements = [];
    for (let index = 0; index < xCheckBoxes.length; index++) {
        if (xCheckBoxes[index].checked) {
            xElements.push(xCheckBoxes[index].value);
        }
    }

    // Валидация X (чекбоксы)
    if (xElements.length === 0) {
        showError(document.querySelector('.checkbox-group'), "Необходимо выбрать значение координаты X :(");
        console.warn("Invalid X value: no selection");
        return false;
    }

    if (xElements.length !== 1) {
        showError(document.querySelector('.checkbox-group'), "Нужно выбрать только 1 значение X :(");
        console.warn("Invalid X value: multiple selection");
        return false;
    }

    // Валидация Y
    if (y === null || y.value.trim() === "") {
        showError(y, "Необходимо ввести значение координаты Y :(");
        console.warn("Invalid Y value: empty");
        return false;
    }

    let normalizedY = y.value.trim().replace(',', '.');
    if (!/^-?\d+(\.\d+)?$/.test(normalizedY)) {
        showError(y, "Необходимо ввести валидное значение Y от -3 до 3 :(");
        console.warn("Invalid Y value:", y.value);
        return false;
    }

    let yNum = parseFloat(normalizedY);
    if (yNum < -3 || yNum > 3) {
        showError(y, "Необходимо ввести валидное значение Y от -3 до 3 :(");
        console.warn("Invalid Y value:", y.value);
        return false;
    }

    // Нормализуем значение в поле ввода для отправки
    y.value = normalizedY;

    // Валидация R
    if (rRadios.length === 0) {
        showError(document.querySelector('.radio-group'), "Необходимо выбрать значение радиуса R :(");
        console.warn("Invalid R value: no selection");
        return false;
    }

    return true;
}

function resetForm() {
    document.getElementById("valForm").reset();
}

function drawPoint(x, y, r, hit) {
    let svgPointX = 100 * parseFloat(x) / r + 150;
    let svgPointY = 150 - parseFloat(y) * 100 / r;
    let svg = document.getElementById("graph");
    let dot = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    dot.setAttribute("cx", svgPointX.toString());
    dot.setAttribute("cy", svgPointY.toString());
    dot.setAttribute("r", "4");
    dot.setAttribute("fill", hit === "true" ? "green" : "red");
    svg.appendChild(dot);
}

function drawPoints(points) {
    console.log('drawing')
    let rRadios = document.querySelectorAll('input[name="r"]:checked');
    if (rRadios.length === 0) return;

    let r = rRadios[0].value;

    for (let i = 0; i < points.length; i++) {
        let point = points[i];
        if (Math.abs(point.x) < r * 1.5 && Math.abs(point.y) < r * 1.5) {
            drawPoint(point.x, point.y, r, point.value);
        }
    }
}

window.onload = () => {
    fetch("init", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({})
    })
        .then(response => response.json())
        .then(data => {
            responses = data;
            showAll(data);
            drawPoints(data);
        }).catch(error => console.error('Error:', error));
}