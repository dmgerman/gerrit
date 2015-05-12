begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GerritConfig
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
name|Provider
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
name|ProvisionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_class
DECL|class|GerritConfigProvider
class|class
name|GerritConfigProvider
implements|implements
name|Provider
argument_list|<
name|GerritConfig
argument_list|>
block|{
DECL|field|servletContext
specifier|private
specifier|final
name|ServletContext
name|servletContext
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritConfigProvider (ServletContext sc)
name|GerritConfigProvider
parameter_list|(
name|ServletContext
name|sc
parameter_list|)
block|{
name|servletContext
operator|=
name|sc
expr_stmt|;
block|}
DECL|method|create ()
specifier|private
name|GerritConfig
name|create
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|GerritConfig
name|config
init|=
operator|new
name|GerritConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setDocumentationAvailable
argument_list|(
name|servletContext
operator|.
name|getResource
argument_list|(
literal|"/Documentation/index.html"
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|GerritConfig
name|get
parameter_list|()
block|{
try|try
block|{
return|return
name|create
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot create GerritConfig instance"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

