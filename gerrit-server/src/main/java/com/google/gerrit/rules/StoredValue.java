begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
package|;
end_package

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SystemException
import|;
end_import

begin_comment
comment|/**  * Defines a value cached in a {@link PrologEnvironment}.  *  * @see StoredValues  */
end_comment

begin_class
DECL|class|StoredValue
specifier|public
class|class
name|StoredValue
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** Construct a new unique key that does not match any other key. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|StoredValue
argument_list|<
name|T
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|StoredValue
argument_list|<
name|T
argument_list|>
argument_list|()
return|;
block|}
comment|/** Construct a key based on a Java Class object, useful for singletons. */
DECL|method|create (Class<T> clazz)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|StoredValue
argument_list|<
name|T
argument_list|>
name|create
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
operator|new
name|StoredValue
argument_list|<
name|T
argument_list|>
argument_list|(
name|clazz
argument_list|)
return|;
block|}
DECL|field|key
specifier|private
specifier|final
name|Object
name|key
decl_stmt|;
comment|/**    * Initialize a stored value key using any Java Object.    *    * @param key unique identity of the stored value. This will be the hash key    *        in the Prolog Environments's hash map.    */
DECL|method|StoredValue (Object key)
specifier|public
name|StoredValue
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
comment|/**    * Initializes a stored value key with a new unique key.    */
DECL|method|StoredValue ()
specifier|public
name|StoredValue
parameter_list|()
block|{
name|key
operator|=
name|this
expr_stmt|;
block|}
comment|/** Look up the value in the engine, or return null. */
DECL|method|getOrNull (Prolog engine)
specifier|public
name|T
name|getOrNull
parameter_list|(
name|Prolog
name|engine
parameter_list|)
block|{
return|return
name|get
argument_list|(
operator|(
name|PrologEnvironment
operator|)
name|engine
operator|.
name|control
argument_list|)
return|;
block|}
comment|/** Get the value from the engine, or throw SystemException. */
DECL|method|get (Prolog engine)
specifier|public
name|T
name|get
parameter_list|(
name|Prolog
name|engine
parameter_list|)
block|{
name|T
name|obj
init|=
name|getOrNull
argument_list|(
name|engine
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
comment|//unless createValue() is overridden, will return null
name|obj
operator|=
name|createValue
argument_list|(
name|engine
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SystemException
argument_list|(
literal|"No "
operator|+
name|key
operator|+
literal|" available"
argument_list|)
throw|;
block|}
name|set
argument_list|(
name|engine
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
DECL|method|set (Prolog engine, T obj)
specifier|public
name|void
name|set
parameter_list|(
name|Prolog
name|engine
parameter_list|,
name|T
name|obj
parameter_list|)
block|{
name|set
argument_list|(
operator|(
name|PrologEnvironment
operator|)
name|engine
operator|.
name|control
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/** Perform {@link #getOrNull(Prolog)} on the environment's interpreter. */
DECL|method|get (PrologEnvironment env)
specifier|public
name|T
name|get
parameter_list|(
name|PrologEnvironment
name|env
parameter_list|)
block|{
return|return
name|env
operator|.
name|get
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/** Set the value into the environment's interpreter. */
DECL|method|set (PrologEnvironment env, T obj)
specifier|public
name|void
name|set
parameter_list|(
name|PrologEnvironment
name|env
parameter_list|,
name|T
name|obj
parameter_list|)
block|{
name|env
operator|.
name|set
argument_list|(
name|this
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a value to store, returns null by default. */
DECL|method|createValue (Prolog engine)
specifier|protected
name|T
name|createValue
parameter_list|(
name|Prolog
name|engine
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

