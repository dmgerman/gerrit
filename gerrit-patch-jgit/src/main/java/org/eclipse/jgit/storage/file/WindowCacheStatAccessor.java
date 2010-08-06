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
DECL|package|org.eclipse.jgit.storage.file
package|package
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|storage
operator|.
name|file
package|;
end_package

begin_comment
comment|// Hack to obtain visibility to package level methods only.
end_comment

begin_comment
comment|// These aren't yet part of the public JGit API.
end_comment

begin_class
DECL|class|WindowCacheStatAccessor
specifier|public
class|class
name|WindowCacheStatAccessor
block|{
DECL|method|getOpenFiles ()
specifier|public
specifier|static
name|int
name|getOpenFiles
parameter_list|()
block|{
return|return
name|WindowCache
operator|.
name|getInstance
argument_list|()
operator|.
name|getOpenFiles
argument_list|()
return|;
block|}
DECL|method|getOpenBytes ()
specifier|public
specifier|static
name|long
name|getOpenBytes
parameter_list|()
block|{
return|return
name|WindowCache
operator|.
name|getInstance
argument_list|()
operator|.
name|getOpenBytes
argument_list|()
return|;
block|}
DECL|method|WindowCacheStatAccessor ()
specifier|private
name|WindowCacheStatAccessor
parameter_list|()
block|{   }
block|}
end_class

end_unit

