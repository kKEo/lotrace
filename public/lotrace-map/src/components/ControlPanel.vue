<script setup>
import { ref } from "vue";
import {
    participants,
    chosenParticipant,
    locationsChecked,
    statsChecked,
    timeScope,
    userPointsKeys,
    showYourLocation,
} from "@/stores/mapStore";
import { onMounted, watch } from "vue";

defineProps({
    msg: String,
});

const count = ref(0);
const options = ref([]);

function loadParticipants(minHours, maxHours) {
    console.log("Load participants: ", minHours, " - ", maxHours, "(h)");
    fetch(
        `https://org.maziarz.org/api/participants?minHours=${minHours}&maxHours=${maxHours}`,
    )
        .then((response) => response.json())
        .then((data) => {
            data.forEach(
                (op) =>
                    (op.cached = userPointsKeys.value.includes(op["id"])
                        ? "[--]"
                        : "   "),
            );
            options.value = data;
        });
}

function loadParticipantsFromRange(minHours, maxHours) {
    console.log("Load participants: ", minHours, " - ", maxHours, "(h)");
    fetch(
        `https://org.maziarz.org/api/participants?minHours=${minHours}&maxHours=${maxHours}`,
    )
        .then((response) => response.json())
        .then((data) => {
            data.forEach(
                (op) =>
                    (op.cached = userPointsKeys.value.includes(op["id"])
                        ? "[--]"
                        : "   "),
            );
            options.value = data;
        });
}

onMounted(() => {
    loadParticipants(timeScope.value[0], timeScope.value[1]);
});

watch(chosenParticipant, (newValue) => {
    console.log("Selected option changed to:", newValue);
});

watch(timeScope, (newValue) => {
    loadParticipants(timeScope.value[0], timeScope.value[1]);
});
</script>

<template>
    <div class="card">
        <div
            style="
                width: 100vw;
                max-width: 600px;
                display: flex;
                backgound: yellow;
            "
        >
            <div style="width: 220px; margin-right: 10px">
                Time: {{ timeScope[0] }} - {{ timeScope[1] }}(h)
            </div>
            <el-slider v-model="timeScope" range :min="54" :max="170" />
            <div style="width: 40px"></div>
        </div>
        <div style="display: flex">
            <div>
                <label for="selectControl">Wiślak:</label>
                <select id="selectControl" v-model="chosenParticipant">
                    <option
                        v-for="option in options"
                        :key="option.id"
                        :value="option.id"
                    >
                        {{ option.cached }} {{ option.name }} ({{
                            option.hours
                        }}h)
                    </option>
                </select>
            </div>
            <div style="padding-left: 20px">Id: {{ chosenParticipant }}</div>
        </div>
    </div>
    <div>
        <label>
            <input type="checkbox" v-model="locationsChecked" />
            Show locations
        </label>
        <label>
            <input type="checkbox" v-model="statsChecked" />
            Show stats
        </label>
        <label>
            <input type="checkbox" v-model="showYourLocation" />
            Show your location
        </label>
    </div>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
.card {
    padding: 2px;
}
.card > div {
    margin-left: 20px;
}
</style>
