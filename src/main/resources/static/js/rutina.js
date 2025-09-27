const ctx = document.getElementById('progressChart');

if (historialData && historialData.length > 0) {
  const labels = historialData.map(h => h.fecha);
  const pesos = historialData.map(h => h.pesoUsado);

  new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Peso usado (kg)',
        data: pesos,
        borderColor: 'blue',
        fill: false
      }]
    }
  });
}
