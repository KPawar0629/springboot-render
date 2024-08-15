<!DOCTYPE html>
<html lang="en">
<head>
    <title>Dashboard - Sangam</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

    <style>
        .jumbotron {
            background-color: #343a40;
            color: white;
            padding: 2rem 1rem;
        }
        .jumbotron h1 {
            font-size: 3rem;
            font-weight: 300;
        }
        .dashboard-section {
            text-align: center;
            padding: 20px;
        }
        .dashboard-section h3 {
            margin-bottom: 1rem;
        }
        .dashboard-section p {
            margin-bottom: 1.5rem;
        }
        .dashboard-section a {
            margin-bottom: 1rem;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .btn-outline-success {
            margin-bottom: 0rem;
        }
        .row.flex-row {
            display: flex;
            align-items: stretch;
        }
        .col-md-4 {
            border-right: 1px solid #ddd;
        }
        .col-md-4:last-child {
            border-right: none;
        }
        .card-img-top {
            width: 20px;
        }
    </style>
</head>
<body>

<#include "nav.ftl">
<link rel="stylesheet" href="./css/others.css">

<div class="text-center bg-dark text-light">
    <div class="container-fluid">
        <br>
        <br>
        <h1>Welcome to Sangam</h1>
        <p>Bringing communities Together.</p>
        <br>
        <br>
    </div>
</div>

<div class="container-fluid">
    <div class="row flex-row">
        <div class="col-md-4 dashboard-section">
            <h3>Widgets</h3>
            <a href="#" class="btn btn-primary">Go to Widgets</a>
        </div>
        <div class="col-md-4 dashboard-section">
            <h3>Photo Gallery</h3>
            <a href="#" class="btn btn-primary">View Gallery</a>
        </div>
        <div class="col-md-4 dashboard-section">
            <#if loggedInUser??>
                <a href="/event_list" class="btn btn-outline-success btn-lg"><h3 class="d-inline">Events</h3></a>
            <#else>
                <h3>Events</h3>
            </#if>
            <#list events as event>
                <div class="card w-100 mb-3 d-block">
                    <img src="./imgs/gallery/event01.jpg" class="card-img-top rounded" alt="">
                    <div class="card-body">
                        <h5 class="card-title">
                            <#if loggedInUser??>
                                <a href="/events/edit/${event.eventId}">${event.eventName}</a>
                            <#else>
                                ${event.eventName}
                            </#if>
                        </h5>
                        <h6 class="card-subtitle mb-2 text-body-secondary">${event.eventDescription}</h6>
                        <p class="card-text">&#xf3c5; ${event.eventLocation}<br>${event.eventDateTime}</p>
                        <a href="/tickets/new/${event.eventId}" class="btn btn-primary">Buy Tickets</a>
                    </div>
                </div>
            </#list>
        </div>
    </div>
</div>

<#if message??>
    <div class="modal fade" id="messageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="messageModalLabel">Notification</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>${message}</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</#if>
<script>
    const myModal = new bootstrap.Modal('#messageModal');

    window.addEventListener('DOMContentLoaded', () => {
        myModal.show();
    });
</script>
</body>
</html>
