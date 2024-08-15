<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Buy Tickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="./css/styles.css" rel="stylesheet" type="text/css"/> <!-- Custom CSS for additional styling -->
    <style>
        .container {
            margin-top: 20px;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle;
        }
        .warning-text {
            font-size: 1.2rem;
            font-weight: bold;
        }
        .btn-submit {
            width: 100%;
            font-size: 1.1rem;
        }
        .pricing-input {
            width: 100%;
        }
        .pricing-table {
            margin-bottom: 20px;
        }
        .vertical-line {
            border-left: 2px solid #dee2e6;
            height: 100%;
        }
        .bdr {
            border-radius: 6px;
            border-width: 5px;
            overflow: hidden;
        }
    </style>
</head>
<body>
<#include "nav.ftl">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-10 col-lg-8">
            <form class="row mt-5" action="/add_ticket" method="post" autocomplete="off" id="registration_form">
                <h4 class="text-center mb-4">Buy Tickets for ${event.eventName}</h4>

                <#if (error)??>
                    <div id="error-message" class="alert alert-danger text-center" role="alert">
                        ${error}
                    </div>
                </#if>

                <input type="hidden" name="eventInput" value="${event.eventId}">

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="lastName" class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="lastName" placeholder="John Smith" name="fullName" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email Address</label>
                        <input type="email" class="form-control" id="email" placeholder="JohnSmith@gmail.com" name="email" required>
                    </div>
                </div>

                <div class="col-md-1 d-flex align-items-center justify-content-center">
                    <div class="vertical-line"></div>
                </div>

                <div class="col-md-5">
                    <div class="pricing-table">
                        <table class="table table-bordered bdr" id="pricingTable">
                            <thead>
                            <tr>
                                <th>Pricing Option</th>
                                <th>Price</th>
                                <th>No. of Tickets</th>
                                <th>Cost</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list eventPricings as pricing>
                                <tr>
                                    <td>${pricing.pricingName}</td>
                                    <td>${pricing.pricingRate}</td>
                                    <td><input type="number" step="1" name="pricing_${pricing.id}" class="pricing-input" min="0" max="6"></td>
                                    <td>$0</td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                    <span id="totalText" class="warning-text text-success">Total: $0</span><br>
                    <span id="warningText" class="warning-text text-danger"></span>

                    <button type="submit" class="btn btn-primary btn-submit mt-3">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('registration_form');
        const submitButton = form.querySelector('button[type="submit"]');
        const warningText = document.getElementById('warningText');

        function updateSubTotal() {
            const table = document.getElementById('pricingTable');
            const text = document.getElementById('totalText');
            let sumVal = 0;

            for (let i = 1; i < table.rows.length; i++) {
                const price = parseFloat(table.rows[i].cells[1].innerHTML);
                const numberTimesInput = table.rows[i].cells[2].querySelector('input');

                if (!numberTimesInput) {
                    console.error('Input element not found for row', i);
                    continue;
                }

                let numberTimes = parseInt(numberTimesInput.value, 10);

                if (isNaN(numberTimes) || numberTimes < 0) {
                    numberTimes = 0;
                }

                const rowTotal = price * numberTimes;

                table.rows[i].cells[3].innerHTML = "$" + rowTotal.toFixed(2);
                sumVal += rowTotal;
            }

            text.textContent = 'Total: $' + sumVal.toFixed(2);
        }


        function validateForm() {
            let valid = false;
            let hasInput = false;
            let hasNegative = false;

            // Iterate through all pricing inputs
            form.querySelectorAll('input[name^="pricing_"]').forEach(input => {
                const value = parseInt(input.value, 10);

                if (value > 0) {
                    hasInput = true;  // At least one input has a valid number
                }

                if (value < 0) {
                    hasNegative = true;  // Found a negative number
                }
            });

            if (!hasInput) {
                warningText.textContent = 'Please enter at least one valid number of tickets.';
                valid = false;
            } else if (hasNegative) {
                warningText.textContent = 'Ticket numbers cannot be negative.';
                valid = false;
            } else {
                warningText.textContent = '';  // Clear any previous warning
                valid = true;
            }

            submitButton.disabled = !valid;
        }

        // Validate on input change
        form.addEventListener('input', function() {
            validateForm();
            updateSubTotal();
        });

        // Initial validation
        validateForm();
        updateSubTotal();

    });
</script>
</body>
</html>
