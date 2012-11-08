Name:		${project.name}
Version:	${project.version}
Release:	1%{?dist}
Summary:	CLI tool to set up JON database and trigger install from within a script

Group:		Administration
License:	LGPL
URL:	    ${project.url}
Source0:	%{name}-%{version}.tgz
BuildRoot:	%(mktemp -ud %{_tmppath}/%{name}-%{version}-%{release}-XXXXXX)
Packager:	Romain Pelisse
BuildArch:	noarch

BuildRequires:	/bin/rm, /bin/rmdir, /bin/cp
Requires:	java

%description
${project.description}

%prep
%setup -q


%build

%install
rm -rf $RPM_BUILD_ROOT
mkdir -p %{buildroot}/usr/local/bin/
mkdir -p %{buildroot}/usr/local/java/lib/
cat %{name} | sed -e 's;^setUpClassPath .*$;setUpClassPath "/usr/local/java/lib/";' > %{buildroot}/usr/local/bin/%{name}
cp -p %{name}-%{version}.jar %{buildroot}/usr/local/java/lib/

%clean
rm -rf $RPM_BUILD_ROOT


%files
%defattr(-,root,root,-)
%attr(0755,root,root) /usr/local/bin/%{name}
/usr/local/java/lib/%{name}-%{version}.jar

%changelog
* Mon Mar 19 2012 Romain Pelisse <belaran@gmail.com> 1.0-1
- Initial RPM
