import React from 'react'

interface CardProps {
  title?: string
  children: React.ReactNode
  className?: string
}

export const Card: React.FC<CardProps> = ({ title, children, className = '' }) => {
  return (
    <div className={`bg-telegram-secondary-bg rounded-xl p-4 shadow-sm border border-telegram-text/10 ${className}`}>
      {title && <h3 className="font-semibold mb-3">{title}</h3>}
      {children}
    </div>
  )
}
