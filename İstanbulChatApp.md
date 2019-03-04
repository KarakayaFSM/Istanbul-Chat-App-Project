# İstanbul Chat App

Bu uygulama java ile yazılmış, çoklu kullanıcı desteği sunan
anlık bir mesajlaşma uygulamasıdır.

# Özellikler

 - **Sade Arayüz**
 - Çoklu Kullanıcı Desteği
 - Kullanıcı Adı, Şifre ve Eposta ile Hızlı Kayıt
 - Eposta ve Doğrulama Kodu ile **Şifre Sıfırlama Hizmeti**
 - Çift Tıklama İle Açılıp Kapanan Sekmeler

## Nasıl Çalıştırırım

Bu program şu anda gerçek bir sunucuya sahip olmadığından **localhost** (yerel bilgisayar) üzerinde çalışmaktadır. Programı **denemek için** ilk olarak Sunucu_ServerExecutable klasöründeki 	**server.java** uygulamasından sunucuyu başlatınız, **daha sonra** İstemci_ClientExecutable **ChatApp.java** üzerinden kaydolarak giriş yapabilirsiniz.

## Program Ana Yapısı
**Giriş Ekranı:**

 - Kullanıcı Adı ve Şifre ile Giriş
 - Kaydol Ekranına Geçiş
 - Şifre Sıfırlama Talebi
 
	 Bu sayfa üzerinden yapılır.
		
**Kaydol Ekranı:**
			
 - Kullanıcı Adı, Şifre ve Eposta adresi ile kayıt
 - Sunucu İle Haberleşme ve Girilen Bilgilerin Kontrolü
 
	 Bu sayfa üzerinden yapılır.
 
**Çevrimiçi Kullanıcılar Ekranı:**
		 
 - Sunucuya Bağlı Çevrimiçi Kullanıcılar Listesi 
 - Mesaj Gönderme ve Alma işlemleri

	 Bu sayfa üzerinden yapılır
	
	

 - **Kütüphaneler Ve Kullanım Yerleri**

	 - Sunucu- İstemci Arası İletişim : **JSON** Veri Depolama ve İletim Yapısı
	 - Mesaj geçmişi ve Kullanıcılar veritabanı: **JDBC-SQLite** Dosya Tabanlı Yerel Veritabanı
	 - Şifremi Sıfırlama Anahtarlarının Gönderimi: **Java-Mail-API**

## Neler Öğrendim
	Java dilinde 
 - Multi Threading And Synchronization 
 - Custom Server and Client Socket Implementations
 - Databases
 - Data Structures And Algorithms
 - Request - Response Pattern
 - Basic to Advanced I/O Operations
 - GUI Components
 - Event Handling And Actions
 - Timer Tasks

  Konularında tecrübe kazandım
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTY1NzA4NzU5NywtMTUwMDcxOTIxMyw4NT
AyMzcyMDAsLTI5MDA4Mzk1MCwyMzU3MTIyNjAsLTgzNTY3MjMz
NywtNDI3NjMwNzE0XX0=
-->
