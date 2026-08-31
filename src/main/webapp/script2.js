// Array to store treatments
let billItems = [];

const treatNameInput = document.getElementById('treatName');
const treatPriceInput = document.getElementById('treatPrice');
const addTreatmentBtn = document.getElementById('addTreatmentBtn');
const slipRows = document.getElementById('slipRows');
const slipSubtotal = document.getElementById('slipSubtotal');
const bDiscountInput = document.getElementById('bDiscount');
const slipTotal = document.getElementById('slipTotal');

// Add Treatment Item
addTreatmentBtn.addEventListener('click', () => {
  const name = treatNameInput.value.trim();
  const price = parseFloat(treatPriceInput.value);

  if (!name || isNaN(price) || price <= 0) {
    alert('Please enter a valid treatment name and price');
    return;
  }

  billItems.push({ name, price });
  treatNameInput.value = '';
  treatPriceInput.value = '';
  
  updateBillDisplay();
});

// Remove Treatment Item
function removeItem(index) {
  billItems.splice(index, 1);
  updateBillDisplay();
}

// Calculate and Update UI
function updateBillDisplay() {
  slipRows.innerHTML = '';
  let subtotal = 0;

  billItems.forEach((item, index) => {
    subtotal += item.price;
    const row = document.createElement('div');
    row.className = 'slip-row-item';
    row.innerHTML = `
      <span>${item.name}</span>
      <span>LKR ${item.price.toFixed(2)} 
        <button type="button" class="btn-remove" onclick="removeItem(${index})">&times;</button>
      </span>
    `;
    slipRows.appendChild(row);
  });

  const discount = parseFloat(bDiscountInput.value) || 0;
  const grandTotal = Math.max(0, subtotal - discount);

  slipSubtotal.textContent = `LKR ${subtotal.toFixed(2)}`;
  slipTotal.textContent = `LKR ${grandTotal.toFixed(2)}`;
}

// Listen for Discount Changes
bDiscountInput.addEventListener('input', updateBillDisplay);