<template>
  <div>
    <h3>当日销售统计</h3>

    <!-- First Chart (Hourly Sales) -->
    <el-card style="margin-bottom: 20px;">
      <div ref="hourlyChart" style="width: 100%; height: 200px;"></div>
    </el-card>

    <!-- Filter Buttons -->
    <div style="display: flex; justify-content: center; margin-bottom: 20px;">
      <el-button type="primary" @click="filterData('week')">最近一周</el-button>
      <el-button type="primary" @click="filterData('month')" style="margin-left: 10px;">最近一月</el-button>
    </div>

    <!-- Second Chart (Daily Sales for Week/Month) -->
    <el-card style="margin-bottom: 20px;">
      <div ref="dailyChart" style="width: 100%; height: 200px;"></div>
    </el-card>

    <!-- Date Picker -->
    <el-form label-width="100px" style="margin-bottom: 20px;">
      <el-form-item label="选择日期区间">
        <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="onDateChange"
        />
      </el-form-item>
    </el-form>

    <!-- Third Chart (Bar Chart for Sales Volume) -->
    <el-card>
      <div ref="barChart" style="width: 100%; height: 200px;"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      dateRange: [], // Selected date range for the picker
      hourlyChart: null,
      dailyChart: null,
      barChart: null
    };
  },
  mounted() {
    this.initCharts();
  },
  methods: {
    initCharts() {
      // Initialize Hourly Sales Chart
      this.hourlyChart = echarts.init(this.$refs.hourlyChart);
      this.hourlyChart.setOption({
        title: { text: '小时销售量', left: 'center' },
        xAxis: {
          type: 'category',
          data: ['9点', '10点', '11点', '12点', '13点', '14点', '15点', '16点', '17点']
        },
        yAxis: { type: 'value' },
        series: [
          {
            data: [100, 50, 200, 150, 250, 180, 220, 260, 150],
            type: 'line',
            smooth: true
          }
        ]
      });

      // Initialize Daily Sales Chart
      this.dailyChart = echarts.init(this.$refs.dailyChart);
      this.dailyChart.setOption({
        title: { text: '日销售量', left: 'center' },
        xAxis: {
          type: 'category',
          data: ['9-10', '9-11', '9-12', '9-13', '9-14', '9-15', '9-16']
        },
        yAxis: { type: 'value' },
        series: [
          {
            data: [1500, 2000, 1800, 1300, 1700, 2500, 2200],
            type: 'line',
            smooth: true
          }
        ]
      });

      // Initialize Bar Chart for Sales Volume
      this.barChart = echarts.init(this.$refs.barChart);
      this.barChart.setOption({
        title: { text: '销售量柱状图', left: 'center' },
        xAxis: {
          type: 'category',
          data: ['9-10', '9-11', '9-12', '9-13', '9-14', '9-15', '9-16', '9-17', '9-18']
        },
        yAxis: { type: 'value' },
        series: [
          {
            data: [500, 1000, 800, 1500, 1200, 1600, 900, 1400, 2000],
            type: 'bar',
            itemStyle: {
              color: '#5470c6'
            },
            markLine: {
              data: [{ yAxis: 500, label: { formatter: '基准线：500' } }],
              lineStyle: { color: '#d14a61' }
            }
          }
        ]
      });
    },
    filterData(type) {
      // Filter data logic for recent week or month (dummy data example)
      const data =
          type === 'week'
              ? [1500, 2000, 1800, 1300, 1700, 2500, 2200]
              : [2200, 2100, 2300, 2500, 2400, 2000, 2700];
      this.dailyChart.setOption({
        series: [
          {
            data
          }
        ]
      });
    },
    onDateChange() {
      // Update charts based on selected date range
      console.log('Date range selected:', this.dateRange);
    }
  }
};
</script>

<style scoped>
h3 {
  text-align: center;
  font-weight: bold;
}
.el-card {
  padding: 20px;
}
</style>
