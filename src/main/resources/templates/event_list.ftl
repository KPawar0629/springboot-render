<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</head>
<body>
<#include "nav.ftl">
<link href="./css/others.css" rel="stylesheet" type="text/css"/>

<div class="container mt-3">
    <div class="row">
        <div class="col-sm-4 justify-content-start">
            <h2>Events</h2>
        </div>
        <div class="col-sm-2">
            <a type="button" class="btn btn-success" href="/events/new">Create Event</a>
        </div>
        <div class="col-sm-2 justify-content-end">
            <a type="button" class="btn btn-primary" href="/dashboard">Back to Dashboard</a>
        </div>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>Event Name</th>
            <th>Description</th>
            <th>Date</th>
        </tr>
        </thead>
        <tbody>
            <#list events as event>
                <tr>
                    <td><a href="/events/edit/${event.eventId}">${event.eventName}</a></td>
                    <td>${event.eventDescription}</td>
                    <td>${event.eventDateTime}</td>
                </tr>
            </#list>
        </tbody>
    </table>
</div>

</body>
</html>

