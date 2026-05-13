import "./LoginModal.css";

function LoginModal({ onClose }) {
  return (
    <div className="modal-backdrop">
      <section className="login-card modal-card">
        <button className="modal-close" type="button" onClick={onClose}>
          ×
        </button>

        <h1>Login</h1>
        <p>Let's get back to scoring!</p>

        <form className="login-form">
          <label>
            Username
            <input type="text" name="username" />
          </label>

          <label>
            Password
            <input type="password" name="password" />
          </label>

          <button type="submit">Log In</button>
        </form>
      </section>
    </div>
  );
}

export default LoginModal;