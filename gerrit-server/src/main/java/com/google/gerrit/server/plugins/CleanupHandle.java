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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|ReferenceQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_class
DECL|class|CleanupHandle
class|class
name|CleanupHandle
extends|extends
name|WeakReference
argument_list|<
name|ClassLoader
argument_list|>
block|{
DECL|field|tmpFile
specifier|private
specifier|final
name|File
name|tmpFile
decl_stmt|;
DECL|method|CleanupHandle (File jarFile, ClassLoader ref, ReferenceQueue<ClassLoader> queue)
name|CleanupHandle
parameter_list|(
name|File
name|jarFile
parameter_list|,
name|ClassLoader
name|ref
parameter_list|,
name|ReferenceQueue
argument_list|<
name|ClassLoader
argument_list|>
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|ref
argument_list|,
name|queue
argument_list|)
expr_stmt|;
name|this
operator|.
name|tmpFile
operator|=
name|jarFile
expr_stmt|;
block|}
DECL|method|cleanup ()
name|void
name|cleanup
parameter_list|()
block|{
name|tmpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

