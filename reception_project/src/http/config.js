export const DEVELOP_URL = 'http://121.196.244.13:8080'
export const PRODUCTION_URL = 'http://121.196.244.13:8080'
export default {
  baseURL : process.env.NODE_ENV =='development'?DEVELOP_URL:PRODUCTION_URL
}
