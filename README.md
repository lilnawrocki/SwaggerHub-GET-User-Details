## Description

A Java console application that sends a GET request to SwaggerHub User Management API and saves selected user data to a .csv file. 

Input:

- SwaggerHub owner API Key
- Organization name
- Number of results on page
- Page number

Output:

Comma-separated - first + last name, email, billing type, invite time, start time.

The application uses Gson to convert JSON string into a class object. The application can be modified to include other data in the resulting file.
