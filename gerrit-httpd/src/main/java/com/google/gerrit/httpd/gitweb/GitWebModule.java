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
DECL|package|com.google.gerrit.httpd.gitweb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|gitweb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_class
DECL|class|GitWebModule
specifier|public
class|class
name|GitWebModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|serve
argument_list|(
literal|"/gitweb"
argument_list|)
operator|.
name|with
argument_list|(
name|GitWebServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/gitweb-logo.png"
argument_list|)
operator|.
name|with
argument_list|(
name|GitLogoServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/gitweb.js"
argument_list|)
operator|.
name|with
argument_list|(
name|GitWebJavaScriptServlet
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/gitweb-default.css"
argument_list|)
operator|.
name|with
argument_list|(
name|GitWebCssServlet
operator|.
name|Default
operator|.
name|class
argument_list|)
expr_stmt|;
name|serve
argument_list|(
literal|"/gitweb-site.css"
argument_list|)
operator|.
name|with
argument_list|(
name|GitWebCssServlet
operator|.
name|Site
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

