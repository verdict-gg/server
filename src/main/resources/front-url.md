```js
// app/(auth)/login/page.tsx
'use client';

import { FcGoogle } from 'react-icons/fc';
import { SiNaver } from 'react-icons/si';
import Link from 'next/link';
import { Button } from '@/components/common';

export default function LoginPage() {
    const handleOAuthLogin = (provider: 'google' | 'naver') => {
        window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
        // window.location.href = `https://verlol.site/oauth2/authorization/${provider}`;

    };

    return (
        <div className="min-h-screen bg-[#f5f5f5] flex items-center justify-center px-4">
            <div className="flex flex-col items-center w-full max-w-xs space-y-8">
                {/* 로고 */}
                <Link href="/">
                    <h1 className="text-4xl font-bold tracking-tight text-primary">
                        Verdict.gg
                    </h1>
                </Link>

                {/* 로그인 버튼들 */}
                <div className="flex flex-col w-full gap-3">
                    <Button
                        onClick={() => handleOAuthLogin('google')}
                        className="flex items-center justify-center gap-3 bg-black text-white py-2 rounded-md hover:opacity-90 transition"
                    >
                        <FcGoogle size={30} />
                        <span className="text-sm">구글로 로그인</span>
                    </Button>

                    <Button
                        onClick={() => handleOAuthLogin('naver')}
                        className="flex items-center justify-center gap-3 bg-black text-white py-2 rounded-md hover:opacity-90 transition"
                    >
                        <SiNaver size={20} />
                        <span className="text-sm">네이버로 로그인</span>
                    </Button>
                </div>

                {/* 하단 링크 */}
                <div className="text-xs text-gray-500 space-x-4 mt-4">
                    <Link href="/terms" className="hover:underline">
                        이용약관
                    </Link>
                    <Link href="/privacy" className="hover:underline">
                        개인정보처리방침
                    </Link>
                </div>
            </div>
        </div>
    );
}
```

