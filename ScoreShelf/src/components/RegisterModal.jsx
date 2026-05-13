import "./RegisterModal.css";

function RegisterModal({ onClose }) {
  return (
    <div className="modal-backdrop">
      <section className="register-card">
        <button
          className="modal-close"
          type="button"
          onClick={onClose}
        >
          ×
        </button>

        <h1>Sign Up</h1>
        <p>Let's start scoring!</p>

        <form className="register-form">
          <label>
            Username
            <input type="text" name="username" />
          </label>

          <label>
            Email
            <input type="email" name="email" />
          </label>

          <label>
            Password
            <input type="password" name="password" />
          </label>

          <label>
            Confirm Password
            <input
              type="password"
              name="confirmPassword"
            />
          </label>

          <button type="submit">
            Sign Up
          </button>
        </form>
      </section>
    </div>
  );
}

export default RegisterModal;