import React from 'react'
import { Routes, Route, Link, useLocation } from 'react-router-dom'
import { HomePage } from './pages/HomePage'
import { PlansPage } from './pages/PlansPage'
import { OrdersPage } from './pages/OrdersPage'
import { HealthPage } from './pages/HealthPage'
import { NotFoundPage } from './pages/NotFoundPage'

export const App: React.FC = () => {
  return (
    <div className="min-h-screen bg-telegram-bg text-telegram-text">
      <main className="max-w-4xl mx-auto px-4 py-6">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/plans" element={<PlansPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/health" element={<HealthPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </main>
    </div>
  )
}
