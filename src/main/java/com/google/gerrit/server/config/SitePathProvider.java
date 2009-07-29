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
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/** Provides {@link java.io.File} annotated with {@link SitePath}. */
end_comment

begin_class
DECL|class|SitePathProvider
specifier|public
class|class
name|SitePathProvider
implements|implements
name|Provider
argument_list|<
name|File
argument_list|>
block|{
DECL|field|path
specifier|private
specifier|final
name|File
name|path
decl_stmt|;
annotation|@
name|Inject
DECL|method|SitePathProvider (final SystemConfig config)
name|SitePathProvider
parameter_list|(
specifier|final
name|SystemConfig
name|config
parameter_list|)
block|{
specifier|final
name|String
name|p
init|=
name|config
operator|.
name|sitePath
decl_stmt|;
name|path
operator|=
operator|new
name|File
argument_list|(
name|p
operator|!=
literal|null
operator|&&
name|p
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|p
else|:
literal|"."
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|File
name|get
parameter_list|()
block|{
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

