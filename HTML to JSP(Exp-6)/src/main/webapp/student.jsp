
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Information</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 560px;
            margin: 45px auto;
            padding: 24px;
        }

        h1 {
            color: #183b66;
        }

        label {
            display: block;
            margin-top: 14px;
        }

        input {
            box-sizing: border-box;
            width: 100%;
            padding: 10px;
            margin-top: 5px;
        }

        button {
            margin-top: 20px;
            padding: 10px 18px;
            cursor: pointer;
        }
    </style>
</head>

<body>
    <h1>Student Information</h1>

    <form action="${pageContext.request.contextPath}/hello"
          method="post">

        <label for="name">Name:</label>
        <input id="name" type="text" name="name"
               required maxlength="100">

        <label for="email">Email:</label>
        <input id="email" type="email" name="email"
               required maxlength="150">

        <label for="course">Course:</label>
        <input id="course" type="text" name="course"
               required maxlength="100">

        <label for="age">Age:</label>
        <input id="age" type="number" name="age"
               min="1" max="120" required>

        <button type="submit">Register</button>
    </form>
</body>
</html>
