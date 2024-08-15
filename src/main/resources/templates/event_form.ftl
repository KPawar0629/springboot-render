<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create Event</title>
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

<form action="/events" method="post">
    <div class="container mt-3">
        <div class="row">
            <div class="col-sm-4 justify-content-start">
                <h2>${event.eventId?has_content?then('Edit Event', 'Create New Event')}</h2>
            </div>
            <div class="col-sm-2">
                <button type="submit" class="btn btn-success">${event.eventId?has_content?then('Save', 'Add')}</button>
            </div>
            <div class="col-sm-2 justify-content-end">
                <a href="/event_list" type="button" class="btn btn-primary">Back to Events List</a>
            </div>
            <#if event.eventId??>
                <div class="col-sm-2 justify-content-end">
                    <a href="/events/delete/${event.getEventId()}" type="button" class="btn btn-danger" onclick="return confirm('Do you really want to delete this event?')">Delete</a>
                </div>
            </#if>
        </div>



            <#if event.eventId??>
                <input type="hidden" name="eventId" value="${event.eventId}" />
            </#if>

            <div class="mb-3 mt-3">
                <label for="name">Name:</label>
                <input type="text" class="form-control" id="name" value="${event.eventName!}" placeholder="Enter name" name="eventName" required>
            </div>
            <div class="mb-3">
                <label for="description">Description:</label>
                <input type="text" class="form-control" id="name" value="${event.eventDescription!}" placeholder="Enter Description" name="eventDescription" required>
            </div>
            <div class="mb-3">
                <label for="name">Location:</label>
                <input type="text" class="form-control" id="name" value="${event.eventLocation!}" placeholder="Enter Location" name="eventLocation" required>
            </div>
            <div class="mb-3">
                <label for="name">Date & Time:</label>
                <input type="datetime-local" class="form-control" id="name" value="${event.eventDateTime!}" placeholder="Enter Date Time" name="eventDateTime" required>
            </div>
            <div class="mb-3">
                <label for="name">Message to show on buying tickets:</label>
                <textarea class="form-control" rows="5" id="comment" value="${event.notesOnTickets!}" name="notesOnTickets" placeholder="Enter Notes for buying tickets! Remember earlier notes will not show up here!"></textarea>
            </div>
    </div>
</form>

<div class="container mt-3 mb-3">
    <#if event.eventId??>
        <hr>

        <div>
            <div class="row border-bottom-0">
                <div class="col-sm-4">Pricing Details</div>
                <div class="col-sm-2">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addPricing">Add Pricing</button>
                </div>
            </div>
            <table class="table">
                <thead>
                <tr>
                    <th>Ticket Type</th>
                    <th>Description</th>
                    <th>Rate Per Ticket</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <#list eventPricings as pricing>
                    <tr>
                        <td>${pricing.pricingName}</td>
                        <td>${pricing.pricingDesc}</td>
                        <td>${pricing.pricingRate}</td>
                        <td>
                            <a href="#" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#addPricing"
                            data-id="${pricing.id}"
                            data-tickettype = "${pricing.pricingName}"
                            data-description = "${pricing.pricingDesc}"
                            data-rate="${pricing.pricingRate}">Edit</a>
                            <a href="/event_pricing/delete/${pricing.id}" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this pricing?')">Delete</a>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
            <div class="modal fade" id="addPricing">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 class="modal-title">Add Pricing</h3>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form action="/event_pricing" method="post">
                                <input type="hidden" name="eventId" value="${event.eventId}" />
                                <#--                <input type="text" class="form-control" id="name" value="${event.eventName!}" placeholder="Enter name" name="eventName">-->
                                <#--                <input type="hidden" name="eventPricingId" value="${pricing.id!''}" />-->
                                <#if (pricing.id)??>
                                    <input type="hidden" name="eventPricingId" value="${pricing.id}" />
                                </#if>
                                <div class="mb-3">
                                    <label for="ticketType">Ticket Type:</label>
                                    <input type="text" class="form-control" id="ticketType" name="ticketType" value="${(pricing.pricingName)!}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="description">Description:</label>
                                    <input type="text" class="form-control" id="description" name="description" value="${(pricing.pricingDesc)!}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="ratePerTicket">Rate Per Ticket:</label>
                                    <input type="number" step="0.5" class="form-control" id="ratePerTicket" name="ratePerTicket" value="${(pricing.pricingRate)!}" required>
                                </div>
                                <button type="submit" class="btn btn-success" id="addButton">Add</button>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

<#--            <h3>Add or Update Pricing</h3>-->
<#--            <form action="/event_pricing" method="post">-->
<#--                <input type="hidden" name="eventId" value="${event.eventId}" />-->
<#--                <div class="mb-3">-->
<#--                    <label for="ticketType">Ticket Type:</label>-->
<#--                    <input type="text" class="form-control" id="ticketType" name="ticketType" required>-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="description">Description:</label>-->
<#--                    <input type="text" class="form-control" id="description" name="description" required>-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="ratePerTicket">Rate Per Ticket:</label>-->
<#--                    <input type="number" step="0.01" class="form-control" id="ratePerTicket" name="ratePerTicket" required>-->
<#--                </div>-->
<#--                <button type="submit" class="btn btn-success">Save Pricing</button>-->
<#--            </form>-->
        </div>
    </#if>
</div>

<script>
    let addPricingModal = document.getElementById('addPricing');
    addPricingModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        let button = event.relatedTarget;

        // Extract data-* attributes from the button
        let id = button.getAttribute('data-id');
        let ticketType = button.getAttribute('data-tickettype');
        let description = button.getAttribute('data-description');
        let rate = button.getAttribute('data-rate');

        // Use the above data to populate the form fields in the modal
        let modalTitle = addPricingModal.querySelector('.modal-title');
        let ticketTypeInput = addPricingModal.querySelector('#ticketType');
        let descriptionInput = addPricingModal.querySelector('#description');
        let rateInput = addPricingModal.querySelector('#ratePerTicket');
        let submitButton = addPricingModal.querySelector('#addButton');
        let hiddenIdInput = addPricingModal.querySelector('input[name="eventPricingId"]');

        modalTitle.textContent = id ? 'Update Pricing' : 'Add Pricing';
        submitButton.textContent = id ? 'Update' : 'Add';
        ticketTypeInput.value = ticketType;
        descriptionInput.value = description;
        rateInput.value = rate;

        if (id) {
            // If there is an ID, it means we're editing
            if (!hiddenIdInput) {
                hiddenIdInput = document.createElement('input');
                hiddenIdInput.type = 'hidden';
                hiddenIdInput.name = 'eventPricingId';
                addPricingModal.querySelector('form').appendChild(hiddenIdInput);
            }
            hiddenIdInput.value = id;
        } else if (hiddenIdInput) {
            hiddenIdInput.remove();
        }
    });
</script>

</body>
</html>

