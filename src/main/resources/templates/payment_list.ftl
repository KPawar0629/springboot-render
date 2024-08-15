<!DOCTYPE html>
<html lang="en">
<head>
    <title>Receive Payments - Sangam</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <link href="./css/others.css" rel="stylesheet" type="text/css"/>
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

<div class="container mt-3">
    <div class="row mb-3">
        <h3 class="col-12 col-md-6">Receive Payments</h3>
        <div class="col-12 col-md-6">
            <div class="input-group mb-3">
                <span class="input-group-text" id="basic-addon1">&#128270</span>
                <input class="form-control" type="text" id="searchInput" onkeyup="searchFunction()" placeholder="Enter search text here...">
            </div>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered" id="myTable">
            <thead>
            <tr>
                <th>Payment Status</th>
                <th>Name</th>
                <th>No. of Tickets</th>
                <th>Total Cost</th>
                <th>Received By</th>
                <th>Received On</th>
            </tr>
            </thead>
            <tbody>
            <#list ticketers as master>
                <tr>
                    <#if master.paymentReceived == 0>
                        <td><a href="/payment/${master.ticketMasterId}" class="btn btn-primary" onclick="return confirm('Have you received payment from ${master.fullName} for Ticket ${master.ticketMasterId}?')">&dollar;</a></td>
                    <#else>
                        <td><button type="button" class="btn btn-success" disabled>&check;</button></td>
                    </#if>
                    <td>${master.fullName}</td>
                    <td>${master.totalTickets}</td>
                    <td>${master.totalAmount}</td>
                    <#if master.paymentReceivedBy?? && users[master.paymentReceivedBy]??>
                        <td>${users[master.paymentReceivedBy].fullName}</td>
                    <#else>
                        <td></td>
                    </#if>
                    <td>${master.paymentReceivedAt}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
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
        for (i = 1; i < tr.length; i++) {  // Skip header row
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
