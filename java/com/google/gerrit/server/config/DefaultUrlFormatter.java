begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|AbstractModule
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DefaultUrlFormatter
specifier|public
class|class
name|DefaultUrlFormatter
implements|implements
name|UrlFormatter
block|{
DECL|field|canonicalWebUrlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalWebUrlProvider
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicItem
operator|.
name|itemOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|UrlFormatter
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicItem
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|UrlFormatter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultUrlFormatter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Inject
DECL|method|DefaultUrlFormatter (@anonicalWebUrl Provider<String> canonicalWebUrlProvider)
specifier|public
name|DefaultUrlFormatter
parameter_list|(
annotation|@
name|CanonicalWebUrl
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalWebUrlProvider
parameter_list|)
block|{
name|this
operator|.
name|canonicalWebUrlProvider
operator|=
name|canonicalWebUrlProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getWebUrl ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getWebUrl
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|canonicalWebUrlProvider
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

