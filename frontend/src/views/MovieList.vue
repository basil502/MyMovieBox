<script setup>
import { ref } from "vue";
import Header from "../components/Header.vue";
import { Teleport } from "vue";
import { useRouter } from "vue-router";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";
import axios from "axios";
import { useToast } from 'vue-toastification'
const toast = useToast()

const open = ref(false);

const schema = yup.object({
  movie: yup.string().required("Movie title is required"),
  genre: yup.string().required("Genre is required"),
  language: yup.string().required("Language is required"),
  year: yup
    .number()
    .min(1888, "Year must be realistic")
    .max(new Date().getFullYear(), "Year cannot be in the future")
    .required("Release year is required"),
});
const { handleSubmit, resetForm } = useForm({
  validationSchema: schema,
  initialValues: {
    movie: "",
    genre: "",
    year: "",
    language: "",
    movieError: "",
    genreError: "",
    yearError: "",
    languageError: "",
  },
});

const { value: movie, errorMessage: movieError } = useField("movie");
const { value: genre, errorMessage: genreError } = useField("genre");
const { value: year, errorMessage: yearError } = useField("year");
const { value: language, errorMessage: languageError } = useField("language");

const router = useRouter();

const submitMovie = handleSubmit(async (values) => {
  const userId = localStorage.getItem("userId");
  if (!userId) {
    alert("User is not logged in!");
    return;
  }
  try {
    await axios.post("http://localhost:8086/addMovie", {
      title: values.movie,
      genre: values.genre,
      language: values.language,
      status: "To Watch",
      releaseYear: values.year,
      user: {
        id: Number(userId),
      },
    });
    toast.success("Movie Added Successfully!");
    open.value = false;
    resetForm();
  } catch (error) {
    console.error("Failed:", error);
    toast.error("Movie Adding failed. Please try again.");
  }
});

const today = new Date().toISOString().split("T")[0];

const goToWatchPage = () => {
  router.push("/watch");
};

const goToCompleted = () => {
  router.push("/completed");
};
const goToReview = () => {
  router.push("/review");
};
const closeModal = () => {
  open.value = false;
  resetForm();
};
</script>

<template>
  <Header />
  <!-- Banner -->
  <div class="banner">
    <h1>🎥 Lights. Camera. Your List.</h1>
    <p>
      Curate your personal movie library – from thrillers to rom-coms, track them all in
      one place.
    </p>
  </div>

  <!-- Buttons -->
  <div class="grid">
    <button type="button" @click="open = true">➕ Add Movie</button>
    <button type="button" @click="goToWatchPage">🎬 To Watch</button>
    <button type="button" @click="goToCompleted">✅ Watched</button>
    <button type="button" @click="goToReview">📝 Reviews</button>
  </div>

  <!-- Add Movie Modal -->
  <Teleport to="body">
    <div class="modal" v-if="open" @click.self="closeModal">
      <div class="modal-card">
        <div class="close">
          <button class="close-btn" @click="closeModal">✖</button>
        </div>
        <h2>🎞️ Add New Movie</h2>

        <input
          type="text"
          v-model="movie"
          :placeholder="movieError || '🎬 Movie Title'"
          :class="{ 'input-error': movieError }"
        />
        <select v-model="genre" :class="{ 'input-error': genreError }">
          <option disabled value="">🎭 Select Genre</option>
          <option>Action</option>
          <option>Comedy</option>
          <option>Drama</option>
          <option>Horror</option>
          <option>Romance</option>
          <option>Sci-Fi</option>
          <option>Thriller</option>
          <option>Animation</option>
          <option>Documentary</option>
        </select>
        <select v-model="language" :class="{ 'input-error': languageError }">
          <option disabled value="">🌐 Select Language</option>
          <option>English</option>
          <option>Malayalam</option>
          <option>Hindi</option>
          <option>Spanish</option>
          <option>French</option>
          <option>German</option>
          <option>Chinese</option>
          <option>Japanese</option>
          <option>Korean</option>
        </select>

        <input
          type="number"
          v-model="year"
          :placeholder="yearError || '📅 Release Year'"
          :class="{ 'input-error': yearError }"
          min="1888"
          :max="new Date().getFullYear()"
        />

        <button class="save" @click="submitMovie">💾 Save Movie</button>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
/* Layout */
.container {
  top: 0;
  right: 0;
  width: 100%;
  padding: 2rem;
  background: linear-gradient(to right, #2b1055, #7597de);
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #ffffff;
}

/* Banner */
.banner {
  background: rgba(0, 0, 0, 0.4);
  color: #ffffff;
  text-align: center;
  padding: 2rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
  margin-top: 7rem;
  width: 1100px;
  height: 250px;
}

.banner h1 {
  font-size: 3rem;
  margin-bottom: 0.5rem;
  margin-top: 5rem;
}

.banner p {
  font-size: 1.2rem;
  opacity: 0.9;
}

/* Buttons */
.grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-top: 2rem;
}

.grid button {
  background: linear-gradient(135deg, #ff416c, #ff4b2b);
  color: #fff;
  border: none;
  border-radius: 15px;
  padding: 1rem 1.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 6px 15px rgba(255, 75, 43, 0.4);
  transition: transform 0.2s ease, box-shadow 0.3s ease;
}

.grid button:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(255, 75, 43, 0.5);
}

/* Modal */
.modal {
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

.modal-card {
  background: #2e2a44;
  padding: 2rem;
  border-radius: 1rem;
  width: 90%;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  color: #ffffff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.6);
}

.modal-card h2 {
  text-align: center;
  color: #ffd369;
  font-size: 1.5rem;
}

.modal-card input,
.modal-card select {
  background: #1f1b2e;
  border: 1px solid #3a3456;
  color: #fff;
  padding: 0.75rem 1rem;
  border-radius: 10px;
  font-size: 1rem;
}

.modal-card input:focus,
.modal-card select:focus {
  border-color: #ffd369;
  outline: none;
  box-shadow: 0 0 10px #ffd36950;
}

.modal-card .input-error {
  border: 1px solid red;
}

.modal-card .save {
  background: linear-gradient(135deg, #43cea2, #185a9d);
  color: white;
  font-weight: bold;
  border: none;
  padding: 0.8rem 1rem;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.modal-card .save:hover {
  background: linear-gradient(135deg, #2bc0e4, #eaecc6);
  color: #000;
}
.close-btn {
  position: absolute;
  background: none;
  color: white;
  outline: none;
  border: none;
}
.close {
  display: flex;
  justify-content: flex-end;
}
</style>
