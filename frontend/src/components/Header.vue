<template>
  <div class="header">
    <h1>🎬 MyMovieBox</h1>

    <button type="button" class="nav-btn" @click="GoHome">Home</button>
    <button type="button" class="nav-btn" @click="logout">Logout</button>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();

const GoHome = () => {
  router.push("/movielist");
    open.value = false;

};

const logout = () => {
  router.push("/login");
};
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  right: 0;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1rem 2rem;
  background: linear-gradient(to right, #2b1055, #7597de);
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.nav-btn {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s ease;
}

.nav-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}
h1 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  display: flex;
  position: absolute;
  left: 8rem;
  top: 0rem;
  color: white;
}
</style>
