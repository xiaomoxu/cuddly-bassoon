import {NbMenuItem} from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: '基础数据',
    icon: 'nb-tables',
    children: [
      {
        title: '成分数据',
        children: [
          {
            title: '上证50成分',
            link: '/pages/tables/sz50',
          },
          {
            title: '沪深300成分',
            link: '/pages/tables/hs300',
          },
          {
            title: '中证500成分',
            link: '/pages/tables/zz500',
          },
          {
            title: '50&300交集成分',
            link: '/pages/tables/union',
          },
        ],
      },
      {
        title: '个股数据',
        children: [
          {
            title: '沪市A股',
            link: '/pages/tables/sz50',
          },
          {
            title: '深市A股',
            link: '/pages/tables/hs300',
          },
          {
            title: '中小板',
            link: '/pages/tables/zz500',
          },
          {
            title: '创业板',
            link: '/pages/tables/union',
          },
        ],
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
        title: '二八轮动',
        link: '/pages/charts/echarts',
      },
      {
        title: '短期上涨',
        link: '/pages/charts/echarts',
      },
      {
        title: '短期放量',
        link: '/pages/charts/echarts',
      },
      {
        title: '昨日重现',
        link: '/pages/charts/echarts',
      },
      {
        title: '日线上穿',
        link: '/pages/charts/echarts',
      },
      {
        title: '策略回溯',
        link: '/pages/charts/echarts',
      },
    ],
  },
  {
    title: '智能顾投',
    icon: 'nb-bar-chart',
    children: [
      {
        title: 'AI风控与组合',
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
