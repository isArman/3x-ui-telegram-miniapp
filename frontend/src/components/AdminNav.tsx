import React from 'react'
import { Link, useLocation } from 'react-router-dom'

export const AdminNav: React.FC = () => {
  const { pathname } = useLocation()

  const navItems = [
    { path: '/admin/orders', label: 'Orders', icon: '📦' },
    { path: '/admin/plans', label: 'Plans', icon: '📋' },
    { path: '/admin/payments', label: 'Payments', icon: '💳' },
  ]

  return (
    <nav className="bg-telegram-secondary-bg border-b border-telegram-text/10 mb-4">
      <div className="max-w-4xl mx-auto px-4 py-2 flex gap-4 overflow-x-auto no-scrollbar">
        {navItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium whitespace-nowrap transition-colors ${
              pathname === item.path
                ? 'text-telegram-link bg-telegram-link/10'
                : 'text-telegram-hint hover:text-telegram-text'
            }`}
          >
            <span>{item.icon}</span>
            <span>{item.label}</span>
          </Link>
        ))}
      </div>
    </nav>
  )
}
