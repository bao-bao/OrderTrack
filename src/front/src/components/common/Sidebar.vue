<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" 
        background-color="#324157" text-color="#fff" active-text-color="#ffd04b" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index">
                        <template slot="title"><i :class="item.icon"></i>{{ item.title }}</template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="item.index + subItem.index">{{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index">
                        <i :class="item.icon"></i>{{ item.title }}
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                items: [
                    {
                        icon: 'el-icon-bell',
                        index: '/home',
                        title: '首页'
                    },
                    {
                        icon: 'el-icon-document',
                        index: '/order',
                        title: '订单管理',
                        subs: [
                            {
                                index: '/onbusiness',
                                title: '生产中'
                            },
                            {
                                index: '/history',
                                title: '历史记录'
                            }
                        ]
                    },
                    {
                        icon: 'el-icon-news',
                        index: '/employee',
                        title: '职员管理'
                    },
                    {
                        icon: 'el-icon-setting',
                        index: '/price',
                        title: '货物管理'
                    },
                    {
                        icon: 'el-icon-star-off',
                        index: '/self',
                        title: '个人信息'
                    }
                ]
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        width: 15%;
        left: 0;
        top: 70px;
        bottom:0;
        background: #2E363F;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
