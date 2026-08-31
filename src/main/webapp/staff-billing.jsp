<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sunrise Dental Clinic — Appointment &amp; Patient Management</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@500;600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="style.css">
</head>
<body>
      
      
      <!-- ---------- BILLING ---------- -->
      <section class="view" id="view-billing">
        <div class="panel">
          <div class="panel-head">
            <h2>Billing &amp; receipt</h2>
          </div>

          <!-- Patient Details List View -->
          <div class="patient-details-list">
            <div class="detail-row">
              <span class="detail-label">Appointment No:</span>
              <span class="detail-value mono" id="displayApptNo">${sessionScope.apptNo}</span><br><br>
            </div>
            <div class="detail-row">
              <span class="detail-label">Patient Name:</span>
              <span class="detail-value" id="displayPatientName">${sessionScope.patientName}</span><br><br>
            </div>
            <div class="detail-row">
              <span class="detail-label">Date:</span>
              <span class="detail-value" id="displayDate">${sessionScope.date}</span><br><br>
            </div>
            <div class="detail-row">
              <span class="detail-label">Time:</span>
              <span class="detail-value" id="displayTime">${sessionScope.time}</span><br><br>
            </div>
            <div class="detail-row">
              <span class="detail-label">Dentist:</span>
              <span class="detail-value" id="displayDentist">${sessionScope.dentist}</span><br><br>
            </div>
          </div>

          <div id="billingArea" class="billing-area">
            <div class="bill-grid">
              
              <!-- Treatment Items Form -->
              <div class="bill-details" id="billPatientInfo">
                <h3>Select Treatments / Services</h3>
                <div class="treatment-selector">
                  <div class="field">
                    <label class="field-label">Treatment Name</label>
                    <input type="text" id="treatName" placeholder="e.g. Dental Cleaning">
                  </div>
                  <div class="field">
                    <label class="field-label">Price (LKR)</label>
                    <input type="number" id="treatPrice" min="0" placeholder="0.00">
                  </div>
                  <button type="button" id="addTreatmentBtn" class="btn btn-secondary">Add Item</button>
                </div>
              </div>

              <!-- Bill Slip Preview -->
              <div class="bill-slip" id="billSlip">
                <div class="slip-head">
                  <strong>Sunrise Dental Clinic</strong>
                  <span>Colombo, Sri Lanka</span>
                  <span class="mono" id="slipApptNo">APT-001</span>
                </div>
                
                <!-- Dynamic Item List -->
                <div class="slip-rows" id="slipRows"></div>

                <!-- Subtotal -->
                <div class="slip-summary-row">
                  <span>Subtotal:</span>
                  <span class="mono" id="slipSubtotal">LKR 0.00</span>
                </div>

                <!-- Discount Input -->
                <label class="field">
                  <span class="field-label">Discount (LKR)</span>
                  <input type="number" id="bDiscount" min="0" value="0" class="mono">
                </label>

                <!-- Total Payable -->
                <div class="slip-total">
                  <span>Total Payable</span>
                  <strong class="mono" id="slipTotal">LKR 0.00</strong>
                </div>

                <div class="form-actions">
                  <button type="button" id="printBillBtn" class="btn btn-primary btn-block">Print receipt</button>
                </div>
              </div>

            </div>
          </div>
        </div>
      </section>
<script src="script2.js"></script>

    </body>
</html>