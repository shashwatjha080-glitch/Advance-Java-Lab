
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Result</title>

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

        .details {
            line-height: 1.9;
            padding: 16px;
            background: #f2f5f8;
            border-radius: 8px;
        }

        a {
            display: inline-block;
            margin-top: 16px;
        }
    </style>
</head>

<body>
    <h1>Registration Successful</h1>
    <hr>

    <h2>Student Details</h2>

    <div class="details">
        <p><strong>Name:</strong> ${name}</p>
        <p><strong>Email:</strong> ${email}</p>
        <p><strong>Course:</strong> ${course}</p>
        <p><strong>Age:</strong> ${age}</p>
    </div>

    <a href="${pageContext.request.contextPath}/student.jsp">
        Register another student
    </a>
</body>
</html>
