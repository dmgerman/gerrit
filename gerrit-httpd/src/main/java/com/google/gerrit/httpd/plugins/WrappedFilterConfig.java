begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|plugins
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
name|GuiceFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
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
DECL|class|WrappedFilterConfig
class|class
name|WrappedFilterConfig
implements|implements
name|FilterConfig
block|{
DECL|field|context
specifier|private
specifier|final
name|WrappedContext
name|context
decl_stmt|;
DECL|method|WrappedFilterConfig (WrappedContext context)
name|WrappedFilterConfig
parameter_list|(
name|WrappedContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getFilterName ()
specifier|public
name|String
name|getFilterName
parameter_list|()
block|{
return|return
name|GuiceFilter
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getInitParameter (String name)
specifier|public
name|String
name|getInitParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
DECL|method|getInitParameterNames ()
specifier|public
name|Enumeration
name|getInitParameterNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getServletContext ()
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

