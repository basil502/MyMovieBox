<template>
  <div class="login-container">
    <div class="login-card">
      <h1>Login</h1>

      <div class="input-group">
        <input
          type="text"
          v-model="username"
          :placeholder="usernameError || 'Username'"
          :class="{ 'input-error': usernameError }"
        />
      </div>

      <div class="input-group">
        <input
          type="password"
          v-model="password"
          :placeholder="passwordError || 'Password'"
          :class="{ 'input-error': passwordError }"
        />
      </div>
      <button type="button" @click="submitForm">Submit</button>

      <div class="register-section">
        <p>Don't have an account?</p>
        <button @click="goregister">Register</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";
import axios from "axios";
import { useToast } from 'vue-toastification'
const toast = useToast()

const schema = yup.object({
  username: yup.string().required("Username is required"),
  password: yup.string().required("Password is required"),
});

const { handleSubmit } = useForm({
  validationSchema: schema,
  initialValues: {
    username: "",
    password: "",
  },
});

const { value: username, errorMessage: usernameError } = useField("username");
const { value: password, errorMessage: passwordError } = useField("password");

//  Router instance
const router = useRouter();

//  Submit handler
const submitForm = handleSubmit(async (values) => {
  try {
    const response = await axios.post("http://localhost:8086/login", {
      username: values.username,
      password: values.password,
    });

    const userId = response.data.userId;
    localStorage.setItem("userId", userId);

    toast.success('Login Successful');
    router.push("/movielist");
  } catch (error) {
    console.error("Login failed:", error);
    toast.error("Login failed. Please try again.");
  }
});

//  Navigate to register
const goregister = () => {
  router.push("/register");
};
</script>

<style scoped>
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(18, 18, 30, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.login-card {
  background: #2e2a44;
  padding: 2rem;
  border-radius: 1rem;
  width: 90%;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  color: #ffffff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.6);
}

.login-card h1 {
  font-size: 32px;
  margin-bottom: 10px;
}

.login-card h1 {
  font-size: 32px;
  margin-bottom: 10px;
  text-align: center;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

input {
  background: #1f1b2e;
  border: 1px solid #3a3456;
  color: #fff;
  padding: 0.75rem 1rem;
  border-radius: 10px;
  font-size: 1rem;
}

input:focus {
  border-color: #ffd369;
  outline: none;
  box-shadow: 0 0 10px #ffd36950;
}

button {
  background: linear-gradient(135deg, #ff416c, #ff4b2b);
  color: white;
  font-weight: bold;
  border: none;
  padding: 0.8rem 1rem;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1rem;
  box-shadow: 0 6px 15px rgba(255, 75, 43, 0.4);
  transition: transform 0.2s ease, box-shadow 0.3s ease;
}

button:hover {
  background-color: #f40612;
}
.register-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.95rem;
  margin-top: 1rem;
}

.register-section button {
  background: transparent;
  color: hsl(204, 59%, 61%);
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 10rem;
  position: absolute;
  box-shadow: none;
  outline: none;
}

.register-section button:hover {
  background: transparent;
  color: #91e1fa;
}

.input-error {
  border: 1px solid red;
  color: red;
}
</style>
