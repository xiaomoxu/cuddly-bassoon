import {NbMenuItem} from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: '成分数据',
    icon: 'nb-tables',
    children: [
      {
        title: '上证50',
        link: '/pages/tables/sz50',
      },
      {
        title: '沪深300',
        link: '/pages/tables/hs300',
      },
      {
        title: '中证500',
        link: '/pages/tables/zz500',
      },
      {
        title: '50&300交集',
        link: '/pages/tables/union',
      },
      {
        title: '沪市A股',
        // link: '/pages/tables/sz50',
      },
      {
        title: '深市A股',
        // link: '/pages/tables/hs300',
      },
      {
        title: '中小板',
        // link: '/pages/tables/zz500',
      },
      {
        title: '创业板',
        // link: '/pages/tables/union',
      },
    ],
  },
  {
    title: '分类数据',
    icon: 'nb-tables',
    children: [
      {
        title: '行业分类',
        link: '/pages/tables/sz50',
      },
      {
        title: '概念分类',
        link: '/pages/tables/hs300',
      },
      {
        title: '地域分类',
        link: '/pages/tables/zz500',
      },
    ],
  },
  {
    title: '基本面数据',
    icon: 'nb-tables',
    children: [
      {
        title: '业绩报告',
        // link: '/pages/tables/sz50',
      },
      {
        title: '营运能力',
        // link: '/pages/tables/hs300',
      },
      {
        title: '成长能力',
        // link: '/pages/tables/zz500',
      },
    ],
  },
  {
    title: '决策工具',
    icon: 'nb-bar-chart',
    children: [
      {
        title: 'K线图',
        link: '/pages/charts/echarts',
      },
      {
        title: '短期放量',
        // link: '/pages/charts/echarts',
      },
      {
        title: '昨日重现',
        // link: '/pages/charts/echarts',
      },
      {
        title: '二八轮动',
        // link: '/pages/charts/echarts',
      },
    ],
  },
  {
    title: '智能顾投',
    icon: 'fa fa fa-chart-line',
    children: [
      {
        title: '风险评估',
        link: '/pages/charts/echarts',
      },
      {
        title: '智能组合',
        link: '/pages/charts/echarts',
      },
      {
        title: '未来预期',
        link: '/pages/charts/echarts',
      },
    ],
  },
  //   {
  //   title: '数据曲线',
  //   icon: 'nb-bar-chart',
  //   children: [
  //     {
  //       title: '曲线汇总',
  //       link: '/pages/charts/echarts',
  //     },
  //   ],
  // },
  {
    title: 'Auth',
    icon: 'nb-locked',
    children: [
      {
        title: 'Login',
        link: '/auth/login',
      },
      {
        title: 'Register',
        link: '/auth/register',
      },
      {
        title: 'Request Password',
        link: '/auth/request-password',
      },
      {
        title: 'Reset Password',
        link: '/auth/reset-password',
      },
    ],
  },
];
