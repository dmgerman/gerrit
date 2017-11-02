begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.httpd.template
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|template
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|FileUtil
operator|.
name|lastModified
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|HtmlDomUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|SiteHeaderFooter
specifier|public
class|class
name|SiteHeaderFooter
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SiteHeaderFooter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|refreshHeaderFooter
specifier|private
specifier|final
name|boolean
name|refreshHeaderFooter
decl_stmt|;
DECL|field|sitePaths
specifier|private
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|template
specifier|private
specifier|volatile
name|Template
name|template
decl_stmt|;
annotation|@
name|Inject
DECL|method|SiteHeaderFooter (@erritServerConfig Config cfg, SitePaths sitePaths)
name|SiteHeaderFooter
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|)
block|{
name|this
operator|.
name|refreshHeaderFooter
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"site"
argument_list|,
literal|"refreshHeaderFooter"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|sitePaths
operator|=
name|sitePaths
expr_stmt|;
try|try
block|{
name|Template
name|t
init|=
operator|new
name|Template
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|t
operator|.
name|load
argument_list|()
expr_stmt|;
name|template
operator|=
name|t
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load site header or footer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parse (Class<?> clazz, String name)
specifier|public
name|Document
name|parse
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Template
name|t
init|=
name|template
decl_stmt|;
if|if
condition|(
name|refreshHeaderFooter
operator|&&
name|t
operator|.
name|isStale
argument_list|()
condition|)
block|{
name|t
operator|=
operator|new
name|Template
argument_list|(
name|sitePaths
argument_list|)
expr_stmt|;
try|try
block|{
name|t
operator|.
name|load
argument_list|()
expr_stmt|;
name|template
operator|=
name|t
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot refresh site header or footer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|t
operator|=
name|template
expr_stmt|;
block|}
block|}
name|Document
name|doc
init|=
name|HtmlDomUtil
operator|.
name|parseFile
argument_list|(
name|clazz
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|injectCss
argument_list|(
name|doc
argument_list|,
literal|"gerrit_sitecss"
argument_list|,
name|t
operator|.
name|css
argument_list|)
expr_stmt|;
name|injectXml
argument_list|(
name|doc
argument_list|,
literal|"gerrit_header"
argument_list|,
name|t
operator|.
name|header
argument_list|)
expr_stmt|;
name|injectXml
argument_list|(
name|doc
argument_list|,
literal|"gerrit_footer"
argument_list|,
name|t
operator|.
name|footer
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
DECL|method|injectCss (Document doc, String id, String content)
specifier|private
name|void
name|injectCss
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|Element
name|e
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|content
argument_list|)
condition|)
block|{
while|while
condition|(
name|e
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|removeChild
argument_list|(
name|e
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|removeAttribute
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|e
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createCDATASection
argument_list|(
literal|"\n"
operator|+
name|content
operator|+
literal|"\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|injectXml (Document doc, String id, Element d)
specifier|private
name|void
name|injectXml
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|id
parameter_list|,
name|Element
name|d
parameter_list|)
block|{
name|Element
name|e
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|e
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|removeChild
argument_list|(
name|e
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|importNode
argument_list|(
name|d
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|Template
specifier|private
specifier|static
class|class
name|Template
block|{
DECL|field|cssFile
specifier|private
specifier|final
name|FileInfo
name|cssFile
decl_stmt|;
DECL|field|headerFile
specifier|private
specifier|final
name|FileInfo
name|headerFile
decl_stmt|;
DECL|field|footerFile
specifier|private
specifier|final
name|FileInfo
name|footerFile
decl_stmt|;
DECL|field|css
name|String
name|css
decl_stmt|;
DECL|field|header
name|Element
name|header
decl_stmt|;
DECL|field|footer
name|Element
name|footer
decl_stmt|;
DECL|method|Template (SitePaths site)
name|Template
parameter_list|(
name|SitePaths
name|site
parameter_list|)
block|{
name|cssFile
operator|=
operator|new
name|FileInfo
argument_list|(
name|site
operator|.
name|site_css
argument_list|)
expr_stmt|;
name|headerFile
operator|=
operator|new
name|FileInfo
argument_list|(
name|site
operator|.
name|site_header
argument_list|)
expr_stmt|;
name|footerFile
operator|=
operator|new
name|FileInfo
argument_list|(
name|site
operator|.
name|site_footer
argument_list|)
expr_stmt|;
block|}
DECL|method|load ()
name|void
name|load
parameter_list|()
throws|throws
name|IOException
block|{
name|css
operator|=
name|HtmlDomUtil
operator|.
name|readFile
argument_list|(
name|cssFile
operator|.
name|path
operator|.
name|getParent
argument_list|()
argument_list|,
name|cssFile
operator|.
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|header
operator|=
name|readXml
argument_list|(
name|headerFile
argument_list|)
expr_stmt|;
name|footer
operator|=
name|readXml
argument_list|(
name|footerFile
argument_list|)
expr_stmt|;
block|}
DECL|method|isStale ()
name|boolean
name|isStale
parameter_list|()
block|{
return|return
name|cssFile
operator|.
name|isStale
argument_list|()
operator|||
name|headerFile
operator|.
name|isStale
argument_list|()
operator|||
name|footerFile
operator|.
name|isStale
argument_list|()
return|;
block|}
DECL|method|readXml (FileInfo src)
specifier|private
specifier|static
name|Element
name|readXml
parameter_list|(
name|FileInfo
name|src
parameter_list|)
throws|throws
name|IOException
block|{
name|Document
name|d
init|=
name|HtmlDomUtil
operator|.
name|parseFile
argument_list|(
name|src
operator|.
name|path
argument_list|)
decl_stmt|;
return|return
name|d
operator|!=
literal|null
condition|?
name|d
operator|.
name|getDocumentElement
argument_list|()
else|:
literal|null
return|;
block|}
block|}
DECL|class|FileInfo
specifier|private
specifier|static
class|class
name|FileInfo
block|{
DECL|field|path
specifier|final
name|Path
name|path
decl_stmt|;
DECL|field|time
specifier|final
name|long
name|time
decl_stmt|;
DECL|method|FileInfo (Path p)
name|FileInfo
parameter_list|(
name|Path
name|p
parameter_list|)
block|{
name|path
operator|=
name|p
expr_stmt|;
name|time
operator|=
name|lastModified
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
DECL|method|isStale ()
name|boolean
name|isStale
parameter_list|()
block|{
return|return
name|time
operator|!=
name|lastModified
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
