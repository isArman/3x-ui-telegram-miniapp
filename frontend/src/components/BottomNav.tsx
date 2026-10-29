import React from 'react'
import { Link, useLocation } from 'react-router-dom'

export const BottomNav: React.FC = () => {
  const { pathname } = useLocation()

  const navItems = [
    { path: '/', label: 'Home', icon: '🏠' },
    { path: '/plans', label: 'Plans', icon: '📋' },
    { path: '/orders', label: 'Orders', icon: '📦' },
    { path: '/health', label: 'Health', icon: '❤️' },
  ]

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-telegram-secondary-bg border-t border-telegram-text/10 z-50 safe-area-bottom">
      <div className="flex items-center justify-around max-w-md mx-auto px-2 py-2">
        {navItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`flex flex-col items-center gap-0.5 px-3 py-1 rounded-lg text-xs font-medium transition-colors ${
              pathname === item.path
                ? 'text-telegram-link bg-telegram-link/10'
                : 'text-telegram-hint hover:text-telegram-text'
            }`}
          >
            <span className="text-lg">{item.icon}</span>
            <span>{item.label}</span>
          </Link>
        ))}
      </div>
    </nav>
  )
}