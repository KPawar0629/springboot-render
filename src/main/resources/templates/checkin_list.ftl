<!DOCTYPE html>
<html lang="en">
<head>
    <title>Ticket Checkin - Sangam</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <style>
        /* Custom styles for responsiveness */
        @media (max-width: 576px) {
            .table-responsive {
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
<#include "nav.ftl">
<link href="./css/others.css" rel="stylesheet" type="text/css"/>

<div class="container mt-3">
    <div class="row mb-3">
        <h3 class="col-12 col-md-6">Check In Tickets</h3>
        <div class="col-12 col-md-6">
            <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1">&#128270</span>
                <input class="form-control" type="text" id="searchInput" onkeyup="searchFunction()" placeholder="Enter search text here...">
            </div>
        </div>
    </div>

    <form action="/confirmCheckIn" method="post">
        <div class="mb-3">
            <div class="row d-flex align-items-end">
                <button type="submit" class="btn btn-primary mb-5">Check In Selected</button>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-bordered" id="myTable">
                    <thead>
                    <tr>
                        <th>Check In</th>
                        <th>Name</th>
                        <th>Ticket Code</th>
                        <th>Ticket Type</th>


                    </tr>
                    </thead>
                    <tbody>
                    <#list details as detail>

                        <tr>
                            <#if detail.paidStatus == 0>
                                <td><button type="button" class="btn btn-danger" disabled>&dollar;</button></td>
                            <#elseif detail.checkedIn == 1>
                                <td><button type="button" class="btn btn-success" disabled>&check;</button> </td>
                            <#else>
                                <td>
                                    <div>
                                        <input type="checkbox" name="ticketIds" value="${detail.detailId}" class="btn-check" id="check_${detail.detailId}" autocomplete="off">
                                        <label for="check_${detail.detailId}" class="btn btn-outline-primary">&#9678;</label>
                                    </div>
                                </td>
                            </#if>
                            <td>${detail.fullName}</td>
                            <td>${detail.getUniqueCode()}</td>
                            <td>${detail.pricingOptionName}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>

            </div>
        </div>
    </form>

</div>

<script>
    function searchFunction() {
        // Declare variables
        var input, filter, table, tr, td, i, txtValue;
        input = document.getElementById("searchInput");
        filter = input.value.toUpperCase();
        table = document.getElementById("myTable");
        tr = table.getElementsByTagName("tr");

        // Loop through all table rows, and hide those who don't match the search query
        for (i = 0; i < tr.length; i++) {
            td = tr[i].getElementsByTagName("td")[1];
            if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                } else {
                    tr[i].style.display = "none";
                }
            }
        }
    }
</script>

</body>
</html>