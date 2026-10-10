import { useMemo, useState } from 'react'
import { Activity, ArrowDownRight, ArrowUpRight, Bell, CheckCircle2, ChevronDown, CircleHelp, CloudSun, FileText, HandCoins, Leaf, Menu, Search, ShieldCheck, Sprout, Users, WalletCards } from 'lucide-react'

type Farmer = { id: number; name: string; district: string; crop: string; acres: number; amount: number; status: string; score: number }
const initialFarmers: Farmer[] = [
  { id: 1, name: 'Ramesh Kumar', district: 'Nashik', crop: 'Cotton', acres: 3, amount: 500000, status: 'Under review', score: 702 },
  { id: 2, name: 'Suresh Patil', district: 'Pune', crop: 'Sugarcane', acres: 4.5, amount: 350000, status: 'Submitted', score: 638 },
  { id: 3, name: 'Lakshmi Devi', district: 'Solapur', crop: 'Pulses', acres: 2, amount: 180000, status: 'Approved', score: 781 },
  { id: 4, name: 'Arjun Singh', district: 'Amravati', crop: 'Soybean', acres: 6, amount: 600000, status: 'Under review', score: 527 },
  { id: 5, name: 'Meena Bai', district: 'Jalgaon', crop: 'Pulses', acres: 1.5, amount: 150000, status: 'Submitted', score: 664 }
]
const inr = (n: number) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(n)
export default function App() {
  const [active, setActive] = useState('Overview')
  const [search, setSearch] = useState('')
  const [language, setLanguage] = useState('EN')
  const [showLogin, setShowLogin] = useState(false)
  const [selected, setSelected] = useState<Farmer>(initialFarmers[0])
  const [analysis, setAnalysis] = useState<{score:number;category:string;foir:number;dti:number;factors:Record<string,number>} | null>(null)
  const [solutions, setSolutions] = useState<string[]>([])
  const [notice, setNotice] = useState('')
  const filtered = useMemo(() => initialFarmers.filter(f => `${f.name} ${f.district} ${f.crop} ${f.status}`.toLowerCase().includes(search.toLowerCase())), [search])
  async function callApi(path: string, options?: RequestInit) {
    try {
      const response = await fetch(`http://localhost:8080${path}`, { ...options, headers: { 'Content-Type': 'application/json', ...(options?.headers || {}) } })
      if (!response.ok) throw new Error(`API returned ${response.status}`)
      return await response.json()
    } catch {
      return null
    }
  }
  async function runAssessment() {
    const payload = { annualIncome: 360000, annualExpenses: 240000, existingDebt: 60000, requestedLoan: selected.amount, tenureMonths: 12, onTimeLoans: 2, incomeVariancePct: 12, landOwned: true, irrigationAvailable: true, cropDiversification: true, insuranceActive: false, droughtRisk: 25, priceCrashRisk: false, existingDefault: false }
    const result = await callApi('/api/analyze/credit-risk', { method: 'POST', body: JSON.stringify(payload) })
    if (result) {
      setAnalysis({ score: result.creditScore, category: result.category, foir: result.foirPercent, dti: result.dtiPercent, factors: result.factorContributions })
      setNotice('Assessment completed by the backend service.')
    } else {
      setAnalysis({ score: selected.score, category: selected.score >= 750 ? 'A' : selected.score >= 650 ? 'B' : selected.score >= 550 ? 'C' : 'D', foir: 34, dti: 16.7, factors: { repaymentHistory: 60, incomeStability: 45, landOwnership: 40, irrigation: 30, noInsurancePenalty: -40 } })
      setNotice('Showing demo assessment. Start the backend services to use the live API.')
    }
  }
  async function executeSolutions() {
    const result = await callApi(`/api/solutions/${selected.id}/execute`, { method: 'POST', body: JSON.stringify({ farmerId: String(selected.id) }) })
    const items = result?.actions?.map((x: {title:string}) => x.title) || ['Government scheme matching', 'Crop insurance guidance', 'Crop-plan diversification', 'Loan restructuring review', 'Market linkage', 'Early-warning protection']
    setSolutions(items)
    setNotice(result ? 'Solution recommendations created by the backend service.' : 'Showing sample recommendations. Start the solution service to persist recommendations.')
  }
  const nav = [{ label: 'Overview', icon: Activity }, { label: 'Loan applications', icon: FileText }, { label: 'Farmer profiles', icon: Users }, { label: 'Risk assessment', icon: ShieldCheck }, { label: 'Solution engine', icon: Sprout }]
  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><Sprout size={23}/></div><div><strong>AgriCred <span>3.0</span></strong><small>AGRICULTURAL FINANCE</small></div></div>
      <div className="workspace-label">WORKSPACE</div>
      <nav>{nav.map(item => <button key={item.label} className={`nav-item ${active===item.label?'active':''}`} onClick={() => setActive(item.label)}><item.icon size={18}/><span>{item.label}</span>{item.label==='Loan applications' && <em>8</em>}</button>)}</nav>
      <div className="sidebar-bottom"><div className="support-card"><div className="support-icon"><CircleHelp size={18}/></div><strong>Need support?</strong><p>Review the operator guide and demo workflow.</p><button onClick={() => setNotice('See docs/ARCHITECTURE.md and docs/phases for setup instructions.')}>Open guide <span>↗</span></button></div><div className="profile"><div className="avatar">AO</div><div><strong>AgriCred Officer</strong><small>Loan operations</small></div><ChevronDown size={15}/></div></div>
    </aside>
    <main className="main">
      <header className="topbar"><button className="mobile-menu" aria-label="Menu"><Menu size={20}/></button><div className="crumb">Workspace <span>/</span> <strong>{active}</strong></div><div className="top-actions"><label className="searchbox"><Search size={16}/><input value={search} onChange={e=>setSearch(e.target.value)} placeholder="Search farmers, loans..." /></label><select aria-label="Language" value={language} onChange={e=>setLanguage(e.target.value)}><option value="EN">EN</option><option value="TA">தமிழ்</option><option value="HI">हिन्दी</option></select><button className="icon-button" aria-label="Notifications" onClick={()=>setNotice('You are all caught up on alerts.')}><Bell size={18}/><i/></button><div className="avatar small">AO</div></div></header>
      <div className="content">
        <section className="welcome"><div><div className="eyebrow"><span className="status-dot"/> AGRICULTURAL LENDING INTELLIGENCE</div><h1>{language==='TA'?'விவசாய நிதி கண்ணோட்டம்':language==='HI'?'कृषि वित्त अवलोकन':'Good morning, Officer'}</h1><p>Review applications, understand risk, and create practical protection pathways for farmers.</p></div><button className="primary-button" onClick={()=>setShowLogin(v=>!v)}><Users size={17}/> {showLogin?'Close demo sign in':'Demo sign in'} </button></section>
        {showLogin && <section className="login-panel"><div><strong>Loan Officer</strong><p>officer@agricred.in</p><small>Demo password: demo123</small></div><div><strong>Farmer</strong><p>ramesh@farmer.in</p><small>Demo password: demo123</small></div><button onClick={async()=>{const r=await callApi('/api/auth/login',{method:'POST',body:JSON.stringify({email:'officer@agricred.in',password:'demo123'})});setNotice(r?'Demo login API responded.':'Start auth-service and API gateway for live login.')}}>Test login API</button></section>}
        {notice && <div className="notice" role="status">{notice}<button onClick={()=>setNotice('')} aria-label="Dismiss">×</button></div>}
        <section className="hero-banner"><div className="hero-orb orb-one"/><div className="hero-orb orb-two"/><div className="hero-content"><div className="hero-kicker"><Leaf size={15}/> SOLUTION-FIRST AGRICULTURAL FINANCE</div><h2>Risk is a signal.<br/><span>Not a final answer.</span></h2><p>Turn a risk assessment into a clear path toward stronger financial resilience.</p><div className="hero-steps"><span><b>01</b> Detect</span><i/><span><b>02</b> Solve</span><i/><span><b>03</b> Transform</span></div></div><div className="hero-graphic"><div className="graphic-ring ring-a"/><div className="graphic-ring ring-b"/><div className="graphic-core"><Sprout size={48}/></div><div className="float-card"><ShieldCheck size={17}/><div><strong>Farmer-first</strong><small>Protection pathway</small></div></div></div></section>
        <section className="stats-grid">
          <Stat icon={<FileText size={19}/>} label="Total applications" value="24" delta="+12.5%" up />
          <Stat icon={<Activity size={19}/>} label="Pending review" value="08" delta="Needs attention" />
          <Stat icon={<ShieldCheck size={19}/>} label="High risk flagged" value="03" delta="Assessment required" />
          <Stat icon={<CheckCircle2 size={19}/>} label="Approved today" value="05" delta="+2 from yesterday" up />
        </section>
        <section className="section-heading"><div><div className="eyebrow">APPLICATION PIPELINE</div><h2>Loan applications</h2><p>Prioritize reviews with clear borrower context.</p></div><button className="outline-button" onClick={()=>setActive('Loan applications')}>View all applications <span>→</span></button></section>
        <section className="table-card"><div className="table-toolbar"><div><strong>Recent applications</strong><span className="table-sub">Sample records · connect the API for live data</span></div><button className="filter-button"><span>All statuses</span><ChevronDown size={15}/></button></div><div className="table-wrap"><table><thead><tr><th>FARMER</th><th>CROP / LAND</th><th>REQUESTED</th><th>CREDIT SCORE</th><th>STATUS</th><th></th></tr></thead><tbody>{filtered.map(f=><tr key={f.id} onClick={()=>setSelected(f)} className={selected.id===f.id?'selected-row':''}><td><div className="farmer-cell"><div className="farmer-avatar">{f.name.split(' ').map(n=>n[0]).join('')}</div><div><strong>{f.name}</strong><small>{f.district}, Maharashtra</small></div></div></td><td><strong>{f.crop}</strong><small className="subline">{f.acres} acres</small></td><td className="money">{inr(f.amount)}</td><td><div className="score-cell"><strong>{f.score}</strong><span className={`score-track ${f.score<550?'low':''}`}><i style={{width:`${f.score/10}%`}}/></span></div></td><td><span className={`pill ${f.status.toLowerCase().replace(' ','-')}`}>{f.status}</span></td><td><button className="row-more" aria-label={`Select ${f.name}`} onClick={e=>{e.stopPropagation();setSelected(f);setActive('Risk assessment')}}>→</button></td></tr>)}</tbody></table>{filtered.length===0&&<div className="empty">No farmers match your search.</div>}</div><div className="table-footer"><span>Showing {filtered.length} of {initialFarmers.length} demo records</span><span>Updated moments ago</span></div></section>
        <section className="lower-grid"><div className="panel"><div className="panel-head"><div><div className="eyebrow">SELECTED BORROWER</div><h3>{selected.name}</h3><p>{selected.crop} · {selected.district} district</p></div><span className="selected-label">Selected</span></div><div className="borrower-metrics"><div><small>Requested amount</small><strong>{inr(selected.amount)}</strong></div><div><small>Land holding</small><strong>{selected.acres} acres</strong></div><div><small>Indicative score</small><strong>{selected.score}<span className="muted"> / 1000</span></strong></div></div><div className="panel-actions"><button className="outline-button" onClick={runAssessment}><Activity size={16}/> Run assessment</button><button className="primary-button" onClick={executeSolutions}><Sprout size={16}/> Create solutions</button></div>{analysis&&<div className="analysis-result"><div className="result-title">Assessment result <span className={`risk-tag ${analysis.category==='D'?'risk-high':''}`}>Category {analysis.category}</span></div><div className="result-grid"><div><small>Credit score</small><strong>{analysis.score}/1000</strong></div><div><small>FOIR estimate</small><strong>{analysis.foir.toFixed(1)}%</strong></div><div><small>DTI estimate</small><strong>{analysis.dti.toFixed(1)}%</strong></div></div><div className="factor-list">{Object.entries(analysis.factors).slice(0,5).map(([k,v])=><div key={k}><span>{k.replace(/([A-Z])/g,' $1')}</span><b className={v<0?'negative':'positive'}>{v>0?'+':''}{v.toFixed(1)}</b></div>)}</div></div>}</div>
          <div className="panel solutions-panel"><div className="panel-head"><div><div className="eyebrow">SOLUTION ENGINE</div><h3>Move from risk to resilience</h3><p>Recommendations for {selected.name}</p></div><div className="solution-icon"><HandCoins size={21}/></div></div>{solutions.length===0?<div className="empty-solutions"><div className="empty-icon"><Sprout size={25}/></div><strong>No solutions generated yet</strong><p>Run the solution engine to create a six-part protection checklist.</p><button className="text-button" onClick={executeSolutions}>Generate recommendations <span>→</span></button></div>:<div className="solution-list">{solutions.map((s,i)=><div key={s} className="solution-row"><span className="solution-number">{String(i+1).padStart(2,'0')}</span><span>{s}</span><CheckCircle2 size={16}/></div>)}</div>}<div className="solution-foot"><ShieldCheck size={16}/><span>Recommendations require farmer consent and authorized review.</span></div></div></section>
        <footer><div className="footer-brand"><Sprout size={18}/><strong>AgriCred 3.0</strong></div><span>Designed for responsible, farmer-first finance.</span><span>Demo environment · Not a lending decision system</span></footer>
      </div>
    </main>
  </div>
}
function Stat({icon,label,value,delta,up}:{icon:React.ReactNode;label:string;value:string;delta:string;up?:boolean}) {
 return <div className="stat-card"><div className="stat-top"><div className="stat-icon">{icon}</div><span className={up?'delta positive-delta':'delta'}>{up?<ArrowUpRight size={13}/>:<ArrowDownRight size={13}/>} {delta}</span></div><div className="stat-value">{value}</div><div className="stat-label">{label}</div></div>
}
