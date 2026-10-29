import React, { createContext, useContext, useEffect, useState } from 'react'

interface ThemeContextType {
  theme: 'light' | 'dark'
  setTheme: (theme: 'light' | 'dark') => void
  telegramTheme: string
}

const ThemeContext = createContext<ThemeContextType>({
  theme: 'light',
  setTheme: () => {},
  telegramTheme: 'light',
})

export const useTheme = () => useContext(ThemeContext)

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [theme, setTheme] = useState<'light' | 'dark'>('light')
  const [telegramTheme, setTelegramTheme] = useState<string>('light')

  useEffect(() => {
    // Get Telegram theme from Telegram WebApp
    const tg = (window as any).Telegram?.WebApp
    if (tg) {
      tg.onThemeChanged((change: any) => {
        setTelegramTheme(change.theme as string)
        if (change.theme === 'dark') {
          setTheme('dark')
        } else {
          setTheme('light')
        }
      })
    }
  }, [])

  const value = React.useMemo(
    () => ({
      theme,
      setTheme,
      telegramTheme,
    }),
    [theme, telegramTheme]
  )

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
}
