import React from 'react'
import { Routes, Route } from 'react-router-dom'
import { HomePage } from './pages/HomePage'
import { PlansPage } from './pages/PlansPage'
import { OrdersPage } from './pages/OrdersPage'
import { OrderCreatePage } from './pages/OrderCreatePage'
import { OrderDetailPage } from './pages/OrderDetailPage'
import { PaymentPage } from './pages/PaymentPage'
import { HealthPage } from './pages/HealthPage'
import { NotFoundPage } from './pages/NotFoundPage'
import { BottomNav } from './components/BottomNav'

export const App: React.FC = () => {
  return (
    <div className="min-h-screen bg-telegram-bg text-telegram-text">
      <main className="max-w-4xl mx-auto px-4 py-6 pb-20">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/plans" element={<PlansPage />} />
          <Route path="/order/:planId" element={<OrderCreatePage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/order/:orderId" element={<OrderDetailPage />} />
          <Route path="/payment/:orderId" element={<PaymentPage />} />
          <Route path="/health" element={<HealthPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </main>
      <BottomNav />
    </div>
  )
}