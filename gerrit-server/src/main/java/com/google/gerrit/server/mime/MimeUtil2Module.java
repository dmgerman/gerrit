begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mime
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mime
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
name|server
operator|.
name|util
operator|.
name|HostPlatform
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
name|Provides
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
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeUtil2
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|detector
operator|.
name|ExtensionMimeDetector
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|detector
operator|.
name|MagicMimeMimeDetector
import|;
end_import

begin_class
DECL|class|MimeUtil2Module
specifier|public
class|class
name|MimeUtil2Module
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
block|{}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|provideMimeUtil2 ()
name|MimeUtil2
name|provideMimeUtil2
parameter_list|()
block|{
name|MimeUtil2
name|m
init|=
operator|new
name|MimeUtil2
argument_list|()
decl_stmt|;
name|m
operator|.
name|registerMimeDetector
argument_list|(
name|ExtensionMimeDetector
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|registerMimeDetector
argument_list|(
name|MagicMimeMimeDetector
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|HostPlatform
operator|.
name|isWin32
argument_list|()
condition|)
block|{
name|m
operator|.
name|registerMimeDetector
argument_list|(
literal|"eu.medsea.mimeutil.detector.WindowsRegistryMimeDetector"
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|registerMimeDetector
argument_list|(
name|DefaultFileExtensionRegistry
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

