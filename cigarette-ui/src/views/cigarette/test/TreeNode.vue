<template>
    <li>
      {{ node.name }}
      <!-- 如果有子节点，则递归渲染子节点 -->
      <ul v-if="isExpanded">
        <tree-node
          v-for="child in node.children"
          :key="child.id"
          :node="child"
          :is-expanded="isExpanded(child)"
        />
      </ul>
    </li>
  </template>
  
  <script>
  export default {
    name: 'TreeNode',
    props: {
      node: Object,
      isExpanded: {
        type: Boolean,
        default: false
      }
    }
  }
  </script>