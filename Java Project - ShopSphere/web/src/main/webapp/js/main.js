/**
 * ShopSphere Core JavaScript
 */
document.addEventListener('DOMContentLoaded', () => {
    // Auto dismiss flash alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Quantity increment/decrement buttons in Cart
    const qtyInputs = document.querySelectorAll('.cart-qty-input');
    qtyInputs.forEach(input => {
        input.addEventListener('change', (e) => {
            const form = e.target.closest('form');
            if (form) form.submit();
        });
    });
});

function copyCoupon(code) {
    const couponInput = document.getElementById('couponCodeInput');
    if (couponInput) {
        couponInput.value = code;
        couponInput.focus();
    }
}
