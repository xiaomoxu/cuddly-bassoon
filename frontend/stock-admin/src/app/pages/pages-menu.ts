import {NbMenuItem} from '@nebular/theme';

export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'nb-home',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: '上证成分',
    icon: 'nb-tables',
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
    title: '数据曲线',
    icon: 'nb-bar-chart',
    children: [
      {
        title: '曲线汇总',
        link: '/pages/charts/echarts',
      },
    ],
  },
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
