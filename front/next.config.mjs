/** @type {import('next').NextConfig} */
const nextConfig = {
  output: "standalone",
  async redirects() {
    return [
      // Basic redirect
      {
        source: "/",
        destination: "/reports",
        permanent: true,
      },
    ];
  },
};

export default nextConfig;
