# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Vue - Official](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (previously Volar) and disable Vetur





select from_start, (from_start-previous_value)/(time_diff)*60*60 from ( select from_start, TIMESTAMPDIFF(SECOND, LAG(position_ts) OVER (ORDER BY position_ts), position_ts) AS time_diff, LAG(from_start) OVER (ORDER BY position_ts) AS previous_value from `w23_points_snapped` order by position_ts) a
