<script setup>
import { ref } from "vue";
import {
    participants,
    chosenParticipant,
    locationsChecked,
    statsChecked,
} from "@/stores/mapStore";
import { onMounted, watch } from "vue";

defineProps({
    msg: String,
});

const count = ref(0);
const options = ref([]);

onMounted(() => {
    fetch("https://org.maziarz.org/api/participants?top=25")
        .then((response) => response.json())
        .then((data) => {
            // console.log(data);
            options.value = data;
        });
});

watch(chosenParticipant, (newValue) => {
    console.log("Selected option changed to:", newValue);
});
</script>

<template>
    <div class="card">
        <div>
            <label for="selectControl">Wiślak:</label>
            <select id="selectControl" v-model="chosenParticipant">
                <option
                    v-for="option in options"
                    :key="option.id"
                    :value="option.id"
                >
                    {{ option.name }} ({{ option.hours }}h)
                </option>
            </select>
        </div>
        <div>Id: {{ chosenParticipant }}</div>
        <div>
            <label>
                <input type="checkbox" v-model="locationsChecked" />
                Show locations
            </label>

            <label>
                <input type="checkbox" v-model="statsChecked" />
                Show stats
            </label>
        </div>
    </div>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
.card {
    padding: 2px;
    display: flex;
}
.card > div {
    margin-left: 20px;
    padding-left: 10px;
    border-left: 1px solid gray;
}
</style>
