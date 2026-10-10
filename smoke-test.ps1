$ErrorActionPreference = "Stop"
$base = "http://localhost:8080"
Write-Host "Checking API gateway routes..."
Invoke-RestMethod "$base/api/farmers" | Out-Null
Invoke-RestMethod "$base/api/loans" | Out-Null
$payload = @{
  annualIncome=360000; annualExpenses=240000; existingDebt=60000; requestedLoan=500000
  tenureMonths=12; onTimeLoans=2; incomeVariancePct=12; landOwned=$true
  irrigationAvailable=$true; cropDiversification=$true; insuranceActive=$false
  droughtRisk=25; priceCrashRisk=$false; existingDefault=$false
} | ConvertTo-Json
$result = Invoke-RestMethod -Method Post -Uri "$base/api/analyze/credit-risk" -ContentType "application/json" -Body $payload
Write-Host "Credit score:" $result.creditScore "Category:" $result.category
Write-Host "Smoke tests passed."
